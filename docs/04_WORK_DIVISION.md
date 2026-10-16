# 04 - 3 Members मधली कामाची वाटणी

Group members: **Member 1**, **Member 2**, **Member 3** (तुमची नावे टाका).

## सुरुवातीला तिघांनी मिळून ठरवायच्या गोष्टी (API Contract)

वाटणी करण्याआधी तिघांनी एकत्र बसून हे ठरवा आणि लिहून ठेवा, म्हणजे काम जोडताना अडचण येत नाही:

1. **URLs** - `02_CODE_EXPLANATION.md` मधील API table (Step 9).
2. **JSON field names** - `holderName`, `email`, `phone`, `accountType`, `initialDeposit`, `amount`, `accountNumber`, `balance`...
3. **Account Rules** - Savings minimum 500, Current overdraft 5000, interest 4%.
4. **Git वापरा** - एक GitHub repository बनवा; प्रत्येकाने स्वतःचा branch (`member1-model`, `member2-backend`, `member3-frontend`) वापरावा.

---

## Member 1 - OOP Model आणि Exceptions (Core OOP)

| काम | Files |
|-----|-------|
| OOP classes | `model/Transactable.java`, `Account.java`, `SavingsAccount.java`, `CurrentAccount.java` |
| Enums आणि Transaction entity | `AccountType.java`, `TransactionType.java`, `BankTransaction.java` |
| Custom exceptions | `exception/AccountNotFoundException`, `InsufficientBalanceException`, `InvalidRequestException`, `DuplicateAccountException` |
| Unit tests | `src/test/.../AccountTest.java` |
| Documentation | Report: OOP implementation chapter; Viva Q&A मधील OOP भाग (प्रश्न 1-12) |

**शिकायचे:** Class, Object, Encapsulation, Inheritance, Abstraction, Polymorphism, interface, abstract class, method overriding, `BigDecimal`, Exceptions.

**Demo मध्ये सांगायचे:** "Account abstract का? `withdraw()` Savings आणि Current साठी वेगळे का वागते?"

---

## Member 2 - Backend: Database, Service, Controller, Validation

| काम | Files |
|-----|-------|
| Project setup | `pom.xml`, `application.properties`, `BankManagementApplication.java` |
| Database layer | `repository/AccountRepository.java`, `TransactionRepository.java` |
| Business logic | `service/AccountService.java`, `service/TransactionService.java` |
| REST API | `controller/AccountController.java` |
| Validation आणि DTO | `dto/` मधील सर्व 5 files |
| Error handling | `exception/GlobalExceptionHandler.java` |
| Documentation | Report: System Design, Database Design, API chapters; Viva Q&A मधील Spring/Database भाग (प्रश्न 13-24) |

**शिकायचे:** Spring Boot, Layered architecture, Dependency Injection, JPA / Hibernate, REST API, `@Valid`, `@Transactional`, H2 console.

**Demo मध्ये सांगायचे:** "एक request Controller -> Service -> Repository -> Database कशी जाते? Validation कुठे होते?"

---

## Member 3 - Frontend, Testing आणि Documentation

| काम | Files |
|-----|-------|
| Web page | `static/index.html` |
| Design | `static/css/style.css` |
| Frontend logic | `static/js/app.js` (fetch, client validation, tables, messages) |
| Testing | `docs/03_TESTING_AND_ERRORS.md` मधले 22 test cases चालवून result लिहिणे; screenshots काढणे |
| Documentation | Final Project Report जुळवणे, PPT बनवणे (`06_PPT_OUTLINE.md` वरून), README, Installation guide |
| Viva तयारी | Viva Q&A मधील Frontend/Testing भाग (प्रश्न 25-30) |

**शिकायचे:** HTML forms, CSS Flexbox, JavaScript `fetch`, `async/await`, JSON, DOM manipulation, XSS पासून संरक्षण (`esc()`).

**Demo मध्ये सांगायचे:** "Frontend backend शी कसे बोलते? दोन ठिकाणी validation का?"

---

## आठवडा-निहाय वेळापत्रक (उदाहरण: 4 आठवडे)

| आठवडा | Member 1 | Member 2 | Member 3 |
|-------|----------|----------|----------|
| 1 | Java OOP revision; `Account`, `SavingsAccount`, `CurrentAccount` समजून घेणे | Spring Boot basics; project setup चालवणे | HTML/CSS/JS revision; `index.html` आणि CSS समजणे |
| 2 | Exceptions, `BankTransaction`, Unit tests चालवणे | Repository, Service समजून चालवणे | `app.js` समजणे; सर्व screens तपासणे |
| 3 | Report: OOP chapter; Viva (OOP) | Controller, Validation, Error handling; Postman testing | 22 test cases + screenshots |
| 4 | **सर्वांनी मिळून:** Integration testing, Report final, PPT, Viva practice (एकमेकांना प्रश्न विचारा) | | |

## सर्वांसाठी महत्त्वाचे

- **प्रत्येकाला संपूर्ण project चालवता आला पाहिजे** आणि पूर्ण flow सांगता आला पाहिजे. Viva मध्ये कोणालाही कोणताही प्रश्न विचारला जाऊ शकतो.
- एकमेकांचा भाग एकमेकांना 15-15 मिनिटे समजावून सांगा.
- Code मधील comments वाचा, ते viva ची तयारी करायला मदत करतात.
