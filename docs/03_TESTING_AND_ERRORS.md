# 03 - Testing आणि Common Errors

## भाग 1: Manual testing (Browser मधून)

Application चालू करा आणि http://localhost:8080 उघडा. खालील test cases एकामागून एक करा.

### Test Cases

| # | Test | Input | अपेक्षित Result |
|---|------|-------|-----------------|
| 1 | Savings account तयार करणे | Name: `Amit Patil`, Email: `amit@example.com`, Phone: `9876543210`, SAVINGS, 1000 | "Account created" + 10 अंकी Account number. Balance Rs. 1,000.00 |
| 2 | Current account तयार करणे | Name: `Sneha Kulkarni`, Email: `sneha@example.com`, Phone: `9123456780`, CURRENT, 2000 | Account तयार |
| 3 | Savings ला opening deposit कमी | SAVINGS, opening 100 | Error: "must be at least Rs. 500.00" |
| 4 | Duplicate email | Test 1 सारखाच email पुन्हा | Error: "An account already exists with this email" |
| 5 | चुकीचे नाव | Name: `A1` | Error: Name validation |
| 6 | चुकीचा phone | `12345` किंवा `5876543210` | Error: Phone must be 10 digits... |
| 7 | चुकीचा email | `amit.com` | Error: valid email |
| 8 | Deposit | Test 1 चा account no., amount 500 | "Deposit of Rs. 500.00 successful. New balance: Rs. 1,500.00" |
| 9 | Withdraw | Savings account, 300 | New balance Rs. 1,200.00 |
| 10 | Minimum balance तोडणे | Savings (balance 1200), withdraw 800 | Error: "Insufficient balance... Maximum you can withdraw now: Rs. 700.00" |
| 11 | Current overdraft | Current (balance 2000), withdraw 6000 | यशस्वी; balance Rs. -4,000.00 |
| 12 | Overdraft limit तोडणे | त्याच Current वरून आणखी 2000 withdraw | Error (limit -5000) |
| 13 | Negative / zero amount | amount `0` किंवा `-50` | Error: amount greater than zero |
| 14 | नसलेला account | `0000000000` | Error 404: Account not found |
| 15 | चुकीची account number लांबी | `123` | Error: exactly 10 digits |
| 16 | Check Balance | Test 1 चा account no. | Passbook card: नाव, balance, minimum balance, monthly interest |
| 17 | Search - नाव | `amit` (लहान अक्षरात) | Amit Patil चा account दिसतो |
| 18 | Search - काही भाग | account number चे पहिले 4 अंक | जुळणारे accounts |
| 19 | Search - रिकामे | काहीच नाही | Error: Enter a name... |
| 20 | Transaction History | Test 1 चा account no. | Opening deposit, deposit, withdraw - नवीन वर |
| 21 | Data टिकतो का | Application बंद करून पुन्हा सुरू करा | सर्व accounts तसेच दिसतात |
| 22 | Server बंद असताना | Application बंद करून Create दाबा | "Cannot connect to the server..." |

### Unit tests (automatic)
```
mvn test
```

## भाग 2: API testing (Postman किंवा curl)

**curl (Mac / Linux / Windows Git Bash):**

```bash
# Account तयार करा
curl -X POST http://localhost:8080/api/accounts -H "Content-Type: application/json" \
  -d '{"holderName":"Amit Patil","email":"amit@example.com","phone":"9876543210","accountType":"SAVINGS","initialDeposit":1000}'

# सर्व accounts
curl http://localhost:8080/api/accounts

# Deposit (1234567890 च्या जागी खरा account number टाका)
curl -X POST http://localhost:8080/api/accounts/1234567890/deposit -H "Content-Type: application/json" -d '{"amount":500}'

# Withdraw
curl -X POST http://localhost:8080/api/accounts/1234567890/withdraw -H "Content-Type: application/json" -d '{"amount":200}'

# Balance
curl http://localhost:8080/api/accounts/1234567890

# Search
curl "http://localhost:8080/api/accounts/search?keyword=amit"

# History
curl http://localhost:8080/api/accounts/1234567890/transactions
```

**Windows Command Prompt (cmd) साठी:** single quote चालत नाही. **Postman** वापरणे सोपे आहे: Method निवडा, URL टाका, Body -> raw -> JSON निवडा.

**Postman Collection:** `POST http://localhost:8080/api/accounts`, Body (raw JSON):
```json
{ "holderName": "Amit Patil", "email": "amit@example.com", "phone": "9876543210",
  "accountType": "SAVINGS", "initialDeposit": 1000 }
```

## भाग 3: Validation Rules ची संपूर्ण list

| Field | Rule |
|-------|------|
| holderName | रिकामे नको; 3-50 अक्षरे; फक्त अक्षरे आणि space |
| email | रिकामे नको; योग्य email format; जास्तीत जास्त 100 अक्षरे; आधी वापरलेला नको |
| phone | रिकामे नको; 10 अंक; 6, 7, 8 किंवा 9 ने सुरू |
| accountType | SAVINGS किंवा CURRENT |
| initialDeposit | रिकामे नको; negative नको; जास्तीत जास्त 2 दशांश; Savings साठी किमान 500 |
| amount (deposit/withdraw) | रिकामे नको; किमान 0.01; जास्तीत जास्त 2 दशांश |
| withdraw | Savings: balance 500 पेक्षा खाली जाऊ नये; Current: -5000 पेक्षा खाली नको |
| account number | 10 अंकी; नसेल तर 404 |

---

## भाग 4: Common Errors आणि उपाय

| # | Error / समस्या | कारण | उपाय |
|---|----------------|------|------|
| 1 | `'java' is not recognized` | Java install नाही किंवा PATH मध्ये नाही | JDK install करा; **नवीन** Command Prompt उघडा; `java -version` तपासा |
| 2 | `'mvn' is not recognized` | Maven PATH मध्ये नाही | Maven `bin` folder `Path` मध्ये add करा; किंवा IntelliJ वापरा |
| 3 | `release version 17 not supported` / `invalid target release: 17` | JDK 17 पेक्षा जुना (उदा. Java 8/11) | JDK 17 किंवा 21 install करा; `JAVA_HOME` त्याकडे दाखवा |
| 4 | `Port 8080 was already in use` | दुसरा program 8080 वापरतोय किंवा application आधीच चालू आहे | जुना application बंद करा, किंवा `application.properties` मध्ये `server.port=8081` करा आणि http://localhost:8081 उघडा |
| 5 | `Could not transfer artifact` / dependencies download होत नाहीत | Internet नाही / college WiFi proxy | Internet तपासा; मोबाईल hotspot वापरून बघा; पुन्हा `mvn spring-boot:run` |
| 6 | `package jakarta.persistence does not exist` (IDE मध्ये लाल रेषा) | Maven dependencies load झाल्या नाहीत | IntelliJ: Maven tab -> Reload; VS Code: "Java: Clean Java Language Server Workspace" |
| 7 | `Database may be already in use` / lock error | दोन application instances एकाच वेळी चालू | सर्व जुने instances बंद करा (Task Manager मध्ये `java`) |
| 8 | Browser मध्ये **Whitelabel Error Page** | `index.html` चुकीच्या folder मध्ये किंवा server चालू नाही | file `src/main/resources/static/index.html` येथेच असावी; console मध्ये `Started...` आले का ते पहा |
| 9 | "Cannot connect to the server" (लाल message) | Application बंद आहे | `mvn spring-boot:run` चालू करा |
| 10 | `Validation failed: ...` | Input नियमांप्रमाणे नाही | Message वाचा; योग्य value टाका (भाग 3 बघा) |
| 11 | `Account not found with number: ...` | Account number चुकीचा | "All Accounts" मधून योग्य number कॉपी करा |
| 12 | `Failed to configure a DataSource` | `application.properties` मध्ये चूक किंवा H2 dependency नाही | `pom.xml` मध्ये h2 आहे का तपासा; properties मधील spelling तपासा |
| 13 | MySQL: `Access denied for user 'root'` | चुकीचा password | `application.properties` मध्ये योग्य password टाका |
| 14 | `Table "ACCOUNTS" not found` (H2 console) | JDBC URL चुकीचा | URL नक्की `jdbc:h2:file:./data/bankdb` असावा; application जिथून चालवला तिथल्याच `data` folder चा database उघडतो |
| 15 | Code बदलला पण browser मध्ये जुनेच दिसते | Browser cache | `Ctrl + F5` (hard refresh); Application restart करा |
| 16 | Tests fail: `NoClassDefFoundError` / तत्सम | Build अपूर्ण | `mvn clean test` चालवा |
| 17 | Application सुरू होतो पण लगेच बंद होतो | Console मध्ये लाल `ERROR` वाचा; बहुतेक वेळा port किंवा database समस्या | वरील 4 आणि 7 बघा |

> **टीप:** कोणताही error आला तर Console/Terminal मधला **शेवटचा `Caused by:`** ओळ वाचा - खरे कारण तिथेच असते.
