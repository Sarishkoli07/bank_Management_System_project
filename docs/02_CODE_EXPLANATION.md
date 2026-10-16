# 02 - Code ची सोपी explanation (Step by Step)

## Step 1: Project चे मोठे चित्र (Architecture)

हा project **Layered Architecture** मध्ये बनवला आहे. प्रत्येक layer चे एकच काम आहे:

```
Browser (index.html + app.js)
        |   JSON (fetch)
        v
Controller   -> browser कडून request घेते, response परत देते
        |
        v
Service      -> bank चे नियम (rules) इथे आहेत
        |
        v
Repository   -> database शी बोलते (SQL Spring आपोआप लिहितो)
        |
        v
Database (H2)  -> tables: accounts, bank_transactions
```

मधे **Model** classes (Account वगैरे) आहेत - त्या data आणि त्याचे behaviour धरून ठेवतात.

**उदाहरण: Deposit केल्यावर काय होते?**
1. User ने account number आणि amount टाकून **Deposit** बटन दाबले.
2. `app.js` आधी तपासते (10 digits आहेत का? amount > 0 आहे का?) आणि `POST /api/accounts/{no}/deposit` request पाठवते.
3. `AccountController.deposit()` ला request मिळते. `@Valid` मुळे `AmountRequest` चे validation होते.
4. `TransactionService.deposit()` account शोधते, `account.deposit(amount)` बोलावते आणि `BankTransaction` record save करते.
5. Response `{amount, balanceAfter, ...}` JSON म्हणून परत येतो आणि `app.js` "Deposit successful. New balance: ..." दाखवते.
6. काही चुकले तर `GlobalExceptionHandler` योग्य error message JSON मध्ये पाठवतो.

---

## Step 2: pom.xml

Maven ची file. यात project ला कोणत्या libraries लागतात ते लिहिले आहे.

| Dependency | काम |
|------------|-----|
| `spring-boot-starter-web` | REST API आणि Tomcat server |
| `spring-boot-starter-data-jpa` | Database शी बोलणे (Hibernate) |
| `spring-boot-starter-validation` | `@NotBlank`, `@Email` सारखे validation |
| `h2` | File based database |
| `spring-boot-starter-test` | JUnit (testing) |

---

## Step 3: application.properties

Server आणि database ची settings.

- `server.port=8080` - application 8080 port वर चालेल.
- `spring.datasource.url=jdbc:h2:file:./data/bankdb` - database `data` folder मध्ये file म्हणून save होईल, म्हणजे application बंद केला तरी data जाणार नाही.
- `spring.jpa.hibernate.ddl-auto=update` - tables आपोआप बनतात.
- `spring.jpa.show-sql=true` - console मध्ये SQL दिसते (शिकण्यासाठी उपयोगी).

---

## Step 4: model package (OOP चे हृदय)

### Transactable.java - Interface (Abstraction)
```java
public interface Transactable {
    void deposit(BigDecimal amount);
    void withdraw(BigDecimal amount);
}
```
फक्त "काय करायचे" सांगतो, "कसे करायचे" नाही. जो class हा implement करेल त्याला दोन्ही methods लिहावे लागतील.

### Account.java - abstract class
- `abstract` म्हणजे `new Account()` करता येत नाही. Savings किंवा Current असे ठराविक account बनवावे लागते.
- **Encapsulation:** सर्व fields `private` आहेत. `balance` बदलायचा असेल तर फक्त `deposit()` / `withdraw()` वापरावे लागते. `setBalance()` मुद्दाम दिले नाही, म्हणजे कोणीही चुकीने balance बदलू शकत नाही.
- **Abstract methods:** `getMinimumBalance()`, `getAccountTypeName()`, `calculateMonthlyInterest()` - child class ने लिहायच्या.
- `withdraw()` मध्ये `getMinimumBalance()` बोलावला आहे. हाच **Polymorphism**: एकच code, पण Savings साठी 500 आणि Current साठी -5000 मिळतो.
- `@Entity`, `@Table`, `@Id`, `@Column` - हे JPA annotations आहेत. ते class ला database table शी जोडतात.
- `BigDecimal` वापरले आहे (double नाही), कारण पैशांच्या हिशोबात double मध्ये rounding चुका होतात (उदा. 0.1 + 0.2 = 0.30000000000000004).

### SavingsAccount.java आणि CurrentAccount.java - Inheritance
`extends Account` लिहिल्यामुळे दोघांना Account चे सर्व fields आणि methods आपोआप मिळतात.

| | SavingsAccount | CurrentAccount |
|---|---|---|
| Minimum balance | Rs. 500 | Rs. -5000 (overdraft) |
| Monthly interest | 4% / 12 | 0 |
| `getAccountTypeName()` | "SAVINGS" | "CURRENT" |

`super(...)` वापरून parent चा constructor बोलावला आहे.

`@DiscriminatorValue("SAVINGS")` मुळे दोन्ही प्रकारचे accounts **एकाच table** (`accounts`) मध्ये save होतात आणि `account_type` column सांगतो कोणता प्रकार आहे.

### BankTransaction.java
प्रत्येक deposit/withdraw चा एक record: account number, type, amount, balanceAfter, description, वेळ.

### AccountType.java / TransactionType.java
`enum` - ठराविक values ची list (SAVINGS/CURRENT आणि DEPOSIT/WITHDRAW). Spelling चुकण्याची शक्यता कमी होते.

---

## Step 5: exception package

| Class | केव्हा येतो | HTTP status |
|-------|-------------|-------------|
| `AccountNotFoundException` | Account number सापडला नाही | 404 |
| `InsufficientBalanceException` | Withdraw ला पैसे कमी | 400 |
| `InvalidRequestException` | चुकीची amount / opening deposit कमी | 400 |
| `DuplicateAccountException` | तोच email आधीच वापरला आहे | 409 |

### GlobalExceptionHandler.java
`@RestControllerAdvice` म्हणजे सर्व controllers चे errors एका जागी पकडले जातात आणि सुंदर JSON message बनतो:
```json
{ "status": 400, "message": "Validation failed", "errors": ["Phone must be 10 digits..."], "timestamp": "..." }
```

---

## Step 6: dto package

**DTO = Data Transfer Object.** Browser आणि server यांच्यात जाणारा data.

- `CreateAccountRequest` - Create Account form चा data. यावर **validation annotations** आहेत:
  - `@NotBlank` - रिकामे चालणार नाही
  - `@Size(min=3, max=50)` - नाव 3 ते 50 अक्षरे
  - `@Pattern` - नावात फक्त अक्षरे आणि space; phone 10 digit आणि 6/7/8/9 ने सुरू
  - `@Email` - योग्य email format
  - `@DecimalMin("0.00")` - opening deposit negative नको
  - `@Digits(integer=10, fraction=2)` - जास्तीत जास्त 2 दशांश अंक
- `AmountRequest` - Deposit/Withdraw ची amount (किमान 0.01).
- `AccountResponse`, `TransactionResponse` - browser ला जाणारा data. Entity थेट पाठवत नाही, कारण database ची रचना लपवायची असते.
- `ErrorResponse` - error चा format.

---

## Step 7: repository package

`AccountRepository extends JpaRepository<Account, Long>` - इतके लिहिले की `save()`, `findAll()`, `findById()` असे methods **आपोआप** मिळतात.

आपण फक्त methods ची नावे लिहिली, SQL Spring लिहितो:
- `findByAccountNumber(String)` -> `SELECT ... WHERE account_number = ?`
- `existsByEmail(String)` -> email आधीच आहे का?
- `search(keyword)` -> `@Query` वापरून नाव/account number/phone मध्ये कुठेही keyword असेल तर शोधतो.

`TransactionRepository.findByAccountNumberOrderByTransactionTimeDescIdDesc` -> त्या account चे transactions, नवीन आधी.

---

## Step 8: service package

### AccountService.java
- `createAccount()`:
  1. Email आधीच आहे का ते तपासतो.
  2. 10 अंकी random account number बनवतो (दुसऱ्या कोणाचा तोच नको याची खात्री करतो).
  3. Account type नुसार `SavingsAccount` किंवा `CurrentAccount` object बनवतो (**Polymorphism**: variable `Account` चा, object child चा).
  4. Savings साठी opening deposit किमान 500 आहे का ते तपासतो.
  5. Save करतो आणि opening deposit चा transaction record बनवतो.
- `getAccount()` - Account शोधतो, नसेल तर `AccountNotFoundException`.
- `searchAccounts()` - Search.
- `getAllAccounts()` - सर्व accounts.

### TransactionService.java
- `deposit()` / `withdraw()` - account शोधतो, `account.deposit()` / `account.withdraw()` बोलावतो, save करतो, transaction record बनवतो.
- `@Transactional` - मधेच काही चुकले तर सर्व database बदल रद्द होतात (rollback). Bank मध्ये हे फार महत्त्वाचे आहे.
- `getHistory()` - Transaction History.

**Constructor Injection:** Service च्या constructor मध्ये Repository दिले आहे; Spring ते आपोआप देतो. (`new` लिहावे लागत नाही - याला **Dependency Injection** म्हणतात.)

---

## Step 9: controller package - AccountController.java

`@RestController` + `@RequestMapping("/api/accounts")`. सर्व URLs:

| Feature | Method | URL |
|---------|--------|-----|
| Create Account | POST | `/api/accounts` |
| सर्व Accounts | GET | `/api/accounts` |
| Search | GET | `/api/accounts/search?keyword=amit` |
| Check Balance | GET | `/api/accounts/{accountNumber}` |
| Deposit | POST | `/api/accounts/{accountNumber}/deposit` |
| Withdraw | POST | `/api/accounts/{accountNumber}/withdraw` |
| Transaction History | GET | `/api/accounts/{accountNumber}/transactions` |

- `@RequestBody` - JSON चे Java object मध्ये रूपांतर.
- `@Valid` - validation चालू करतो.
- `@PathVariable` - URL मधला account number घेतो.

---

## Step 10: frontend (static folder)

Spring Boot `src/main/resources/static` मधल्या files आपोआप दाखवतो, म्हणून `http://localhost:8080` वर `index.html` उघडते.

- **index.html** - 6 panels (All Accounts, Create Account, Deposit/Withdraw, Check Balance, Search, History). डावीकडे menu.
- **style.css** - रंग, layout, mobile साठी responsive design.
- **app.js**:
  - `api()` - server शी बोलणारे common function. Error आला तर `Error` फेकते.
  - `esc()` - user ने टाकलेला text HTML म्हणून चालणार नाही याची काळजी (XSS पासून संरक्षण).
  - प्रत्येक बटनाला `addEventListener` ने function जोडला आहे.
  - Client-side validation आधी, मग server कडे request.
  - `withButton()` - request चालू असताना बटन disable होते (दोनदा click होत नाही).

### Validation दोन ठिकाणी का?
- **Frontend (JavaScript):** user ला लगेच सांगतो; server ला request जात नाही.
- **Backend (Spring):** खरी सुरक्षा. कोणी Postman ने थेट request पाठवली तरी चुकीचा data आत येणार नाही. म्हणून दोन्ही ठिकाणी हवे.

---

## Step 11: Test - AccountTest.java

JUnit ने 6 tests: deposit, withdraw, Savings minimum balance, Current overdraft, चुकीची amount, Polymorphism (interest). Server किंवा database न चालवता फक्त OOP classes तपासतात.
