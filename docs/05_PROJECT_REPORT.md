# Bank Management System

## Project Report

**Course:** ____________________   **Class:** ____________________
**Academic Year:** ____________
**College:** ____________________________________________

**Submitted by:**
1. ______________________ (Roll No. ____ )
2. ______________________ (Roll No. ____ )
3. ______________________ (Roll No. ____ )

**Guide:** ______________________

---

## Certificate

हे प्रमाणित करण्यात येते की वरील विद्यार्थ्यांनी "Bank Management System" हा mini project आमच्या मार्गदर्शनाखाली पूर्ण केला आहे.

Guide: ____________   HOD: ____________   Examiner: ____________

---

## Acknowledgement

आम्ही आमचे guide, विभागप्रमुख आणि महाविद्यालयाचे आभारी आहोत ज्यांनी या project साठी मार्गदर्शन आणि सुविधा दिल्या.

---

## 1. Abstract (सारांश)

Bank Management System हे Java आणि Spring Boot वर आधारित web application आहे. यात Account तयार करणे, Deposit, Withdraw, Balance तपासणे, Account शोधणे आणि Transaction History बघणे या सुविधा आहेत. Project मध्ये OOP च्या संकल्पना (Class, Object, Encapsulation, Inheritance, Abstraction, Polymorphism) प्रत्यक्ष वापरल्या आहेत. Frontend साठी HTML, CSS, JavaScript आणि database साठी H2 वापरला आहे. Input चे validation frontend आणि backend दोन्ही ठिकाणी केले आहे.

## 2. Introduction (प्रस्तावना)

बँकेचे व्यवहार पूर्वी कागदावर होत असत, त्यात वेळ जात असे आणि चुका होत असत. Computerized system मुळे व्यवहार जलद, अचूक आणि सुरक्षित होतात. हा project त्याचे लहान स्वरूप आहे, जेणेकरून OOP आणि web development शिकता येईल.

## 3. Objectives (उद्दिष्टे)

1. OOP संकल्पना प्रत्यक्ष project मध्ये वापरणे.
2. Account आणि Transaction चे व्यवस्थापन computerized करणे.
3. Spring Boot वापरून REST API बनवणे.
4. Database मध्ये data कायमस्वरूपी साठवणे.
5. चुकीचा input थांबवण्यासाठी validation करणे.
6. सोपा आणि वापरायला सुलभ web interface बनवणे.

## 4. Scope (व्याप्ती)

**Project मध्ये आहे:** Create Account (Savings/Current), Deposit, Withdraw, Check Balance, Search Account, Transaction History, Validation, Error handling.

**Project मध्ये नाही (Limitations):** Login / Password, Fund Transfer, Admin आणि Customer roles, Account delete/update, Loan, Statement PDF.

## 5. System Requirements

**Hardware:** Processor: Dual Core किंवा जास्त | RAM: 4 GB (8 GB शिफारस) | Disk: 1 GB मोकळी जागा

**Software:** Windows 10/11, Linux किंवा macOS | JDK 17+ | Maven 3.8+ | IntelliJ IDEA / VS Code | Browser (Chrome/Edge/Firefox)

## 6. Technology Stack

| Layer | Technology | कारण |
|-------|-----------|------|
| Language | Java 17 | OOP शिकण्यासाठी उत्तम |
| Framework | Spring Boot 3.2.5 | कमी configuration मध्ये web application |
| Database access | Spring Data JPA, Hibernate | SQL आपोआप बनते |
| Database | H2 (MySQL ला बदलता येतो) | वेगळे install नको |
| Validation | Jakarta Bean Validation | Annotations ने सोपे validation |
| Frontend | HTML, CSS, JavaScript | Web interface |
| Build | Maven | Dependencies व्यवस्थापन |
| Testing | JUnit 5 | Unit tests |

## 7. System Design

### 7.1 Architecture (Layered)

```
 Browser (HTML / CSS / JavaScript)
        |  HTTP + JSON
 Controller layer   (AccountController)
        |
 Service layer      (AccountService, TransactionService)
        |
 Repository layer   (AccountRepository, TransactionRepository)
        |
 Database (H2)      (accounts, bank_transactions)
```

### 7.2 Class Diagram (OOP)

```
        <<interface>>
        Transactable
        + deposit(amount)
        + withdraw(amount)
              ^
              | implements
     <<abstract>> Account
     - id, accountNumber, holderName
     - email, phone, balance, createdAt
     + deposit(amount)
     + withdraw(amount)
     + getMinimumBalance()        {abstract}
     + getAccountTypeName()       {abstract}
     + calculateMonthlyInterest() {abstract}
        ^                     ^
        | extends             | extends
 SavingsAccount          CurrentAccount
 min balance = 500       min balance = -5000
 interest = 4%/yr        interest = 0

 BankTransaction (id, accountNumber, transactionType, amount, balanceAfter, description, transactionTime)
```

### 7.3 Database Design

**Table: accounts** (Savings आणि Current दोन्ही इथेच - Single Table Inheritance)

| Column | Type | Notes |
|--------|------|-------|
| id | BIGINT | Primary Key, Auto increment |
| account_number | VARCHAR(20) | Unique |
| account_type | VARCHAR | SAVINGS / CURRENT (discriminator) |
| holder_name | VARCHAR(100) | |
| email | VARCHAR(100) | |
| phone | VARCHAR(15) | |
| balance | DECIMAL(15,2) | |
| created_at | TIMESTAMP | |

**Table: bank_transactions**

| Column | Type | Notes |
|--------|------|-------|
| id | BIGINT | Primary Key |
| account_number | VARCHAR(20) | कोणत्या account चा |
| transaction_type | VARCHAR(20) | DEPOSIT / WITHDRAW |
| amount | DECIMAL(15,2) | |
| balance_after | DECIMAL(15,2) | व्यवहारानंतरचा balance |
| description | VARCHAR(200) | |
| transaction_time | TIMESTAMP | |

**Relationship:** एका account चे अनेक transactions (One-to-Many), `account_number` वरून जोडलेले.

### 7.4 REST API

| Feature | Method | URL |
|---------|--------|-----|
| Create Account | POST | /api/accounts |
| All Accounts | GET | /api/accounts |
| Search | GET | /api/accounts/search?keyword=... |
| Check Balance | GET | /api/accounts/{accountNumber} |
| Deposit | POST | /api/accounts/{accountNumber}/deposit |
| Withdraw | POST | /api/accounts/{accountNumber}/withdraw |
| History | GET | /api/accounts/{accountNumber}/transactions |

### 7.5 Flow - Withdraw

1. User account number आणि amount टाकतो -> JavaScript तपासते.
2. `POST /withdraw` -> Controller -> `@Valid` तपासणी.
3. `TransactionService` -> account शोधतो.
4. `Account.withdraw()` -> `getMinimumBalance()` तपासतो (Polymorphism).
5. पैसे पुरेसे असतील तर balance कमी होतो, transaction save होतो; नसतील तर `InsufficientBalanceException` -> 400 error.
6. Browser ला result / error message दिसतो.

## 8. OOP Concepts ची अंमलबजावणी

| Concept | कुठे | कसे |
|---------|------|-----|
| Class | Account, SavingsAccount, CurrentAccount, BankTransaction | वास्तविक वस्तूंचे नमुने |
| Object | `new SavingsAccount(...)` (AccountService मध्ये) | प्रत्येक customer चा वेगळा object |
| Encapsulation | Account चे private fields; setBalance() नाही | Data संरक्षित, बदल फक्त नियमांनुसार |
| Inheritance | `SavingsAccount extends Account` | Code पुन्हा लिहावा लागत नाही |
| Abstraction | `abstract class Account`, `interface Transactable` | फक्त काय करायचे ते ठरवले, कसे ते child ठरवतो |
| Polymorphism | `getMinimumBalance()`, `calculateMonthlyInterest()` overriding | Parent reference ने बोलावले तरी child चे वर्तन चालते |

## 9. Validation

- **Frontend:** JavaScript - नाव, email, phone, amount, account number.
- **Backend:** Bean Validation annotations (`@NotBlank`, `@Size`, `@Pattern`, `@Email`, `@DecimalMin`, `@Digits`).
- **Business rules:** Minimum balance, opening deposit, duplicate email, account अस्तित्वात असणे.
- **Error handling:** `GlobalExceptionHandler` ने सर्व errors चांगल्या JSON message मध्ये.
- **सुरक्षा:** `esc()` function ने XSS थांबवला; पैशांसाठी `BigDecimal`; `@Transactional` ने rollback.

## 10. Testing

**Unit tests (JUnit):** 6 tests - deposit, withdraw, Savings minimum balance, Current overdraft, चुकीची amount, Polymorphism.

**Manual testing:** `03_TESTING_AND_ERRORS.md` मधील 22 test cases.

| Test | Expected | Actual | Status |
|------|----------|--------|--------|
| Create Savings account | Account तयार | (तुम्ही चालवून लिहा) | Pass/Fail |
| Withdraw below minimum | Error | | |
| Deposit | Balance वाढतो | | |
| Search | Account दिसतो | | |
| History | Records दिसतात | | |

## 11. Screenshots

(तुम्ही स्वतः काढा)
1. All Accounts  2. Create Account  3. Validation error  4. Deposit success  5. Withdraw error (minimum balance)  6. Check Balance  7. Search  8. Transaction History  9. H2 Console tables

## 12. Limitations (मर्यादा)

1. Login / Authentication नाही - कोणीही कोणताही account बघू शकतो.
2. Fund Transfer (account to account) नाही.
3. एकाच वेळी दोन users ने एकाच account वर व्यवहार केला तर (concurrency) संरक्षण नाही.
4. Account Update/Delete नाही.

## 13. Future Scope

1. Spring Security ने Login आणि Admin/Customer roles.
2. Fund Transfer आणि Loan module.
3. Account Statement PDF download.
4. Optimistic Locking (`@Version`) concurrency साठी.
5. Email / SMS notification.
6. Cloud वर deploy करणे आणि MySQL / PostgreSQL वापरणे.

## 14. Conclusion

या project मधून आम्ही OOP संकल्पना, Spring Boot, JPA, REST API, Validation आणि web frontend प्रत्यक्ष वापरून शिकलो. Project सर्व आवश्यक सुविधा देतो आणि पुढे वाढवण्यासाठी सोपा आहे.

## 15. References

1. Spring Boot Documentation - https://spring.io/projects/spring-boot
2. Spring Data JPA - https://spring.io/projects/spring-data-jpa
3. Oracle Java Documentation - https://docs.oracle.com/en/java/
4. H2 Database - https://www.h2database.com
5. MDN Web Docs (HTML, CSS, JavaScript, fetch) - https://developer.mozilla.org
6. Maven - https://maven.apache.org
