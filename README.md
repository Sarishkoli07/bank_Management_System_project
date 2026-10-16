# Bank Management System (बँक व्यवस्थापन प्रणाली)

College Mini Project - Java, Spring Boot, HTML, CSS, JavaScript, H2 Database

हा project एक **web-based Bank Management System** आहे. Browser मध्ये उघडून तुम्ही नवीन account काढू शकता, पैसे Deposit / Withdraw करू शकता, Balance बघू शकता, Account Search करू शकता आणि Transaction History बघू शकता.

---

## 1. Features

| # | Feature | कुठे आहे |
|---|---------|----------|
| 1 | Create Account (Savings / Current) | Menu: Create Account |
| 2 | Deposit | Menu: Deposit / Withdraw |
| 3 | Withdraw (minimum balance rule सह) | Menu: Deposit / Withdraw |
| 4 | Check Balance | Menu: Check Balance |
| 5 | Search Account (नाव / account number / phone) | Menu: Search Account |
| 6 | Transaction History | Menu: Transaction History |
| 7 | Validation (Frontend + Backend) | सर्व forms |
| 8 | Graphs - Bar, Pie, Line (Canvas ने हाताने काढलेले, Computer Graphics) | Menu: Graphs |

## 2. OOP Concepts कुठे वापरले आहेत (थोडक्यात)

| Concept | उदाहरण |
|---------|--------|
| Class / Object | `Account`, `SavingsAccount`, `CurrentAccount`, `BankTransaction` |
| Encapsulation | `Account` मधले सर्व fields `private`; `setBalance()` नाही, फक्त `deposit()` / `withdraw()` |
| Inheritance | `SavingsAccount extends Account`, `CurrentAccount extends Account` |
| Abstraction | `abstract class Account`, `interface Transactable` |
| Polymorphism | `getMinimumBalance()` आणि `calculateMonthlyInterest()` प्रत्येक child class मध्ये वेगळे (method overriding) |

## 3. Technology Stack

- **Backend:** Java 17, Spring Boot 3.2.5, Spring Data JPA (Hibernate), Bean Validation
- **Database:** H2 (file based - वेगळे install करायची गरज नाही). MySQL वापरायचे असेल तर `application.properties` मध्ये सूचना दिल्या आहेत.
- **Frontend:** HTML, CSS, JavaScript (`fetch` API)
- **Build tool:** Maven

## 4. Project Folder Structure

```
bank-management-system/
├── pom.xml                                   <- Maven: dependencies ची list
├── README.md                                 <- हीच file
├── .gitignore
├── docs/
│   ├── 01_INSTALL_AND_RUN.md                 <- Install आणि run कसे करायचे
│   ├── 02_CODE_EXPLANATION.md                <- प्रत्येक file ची सोपी explanation
│   ├── 03_TESTING_AND_ERRORS.md              <- Testing आणि common errors
│   ├── 04_WORK_DIVISION.md                   <- 3 members मधली कामाची वाटणी
│   ├── 05_PROJECT_REPORT.md                  <- Project Report
│   ├── 06_PPT_OUTLINE.md                     <- PPT outline
│   ├── 07_VIVA_QA.md                         <- Viva questions आणि answers
│   └── 08_OOPCG_NOTES.md                     <- OOPCG subject साठी notes (OOP + Graphics)
└── src/
    ├── main/
    │   ├── java/com/college/bank/
    │   │   ├── BankManagementApplication.java        <- program इथून सुरू होतो
    │   │   ├── model/
    │   │   │   ├── Transactable.java                 <- interface (Abstraction)
    │   │   │   ├── Account.java                      <- abstract parent class
    │   │   │   ├── SavingsAccount.java               <- child class
    │   │   │   ├── CurrentAccount.java               <- child class
    │   │   │   ├── BankTransaction.java              <- transaction record
    │   │   │   ├── AccountType.java                  <- enum
    │   │   │   └── TransactionType.java              <- enum
    │   │   ├── dto/
    │   │   │   ├── CreateAccountRequest.java         <- form data + validation
    │   │   │   ├── AmountRequest.java                <- deposit/withdraw data + validation
    │   │   │   ├── AccountResponse.java              <- browser ला जाणारा account data
    │   │   │   ├── TransactionResponse.java          <- browser ला जाणारा transaction data
    │   │   │   └── ErrorResponse.java                <- error message format
    │   │   ├── repository/
    │   │   │   ├── AccountRepository.java            <- account database access
    │   │   │   └── TransactionRepository.java        <- transaction database access
    │   │   ├── service/
    │   │   │   ├── AccountService.java               <- create / search / balance logic
    │   │   │   └── TransactionService.java           <- deposit / withdraw / history logic
    │   │   ├── controller/
    │   │   │   └── AccountController.java            <- REST API URLs
    │   │   └── exception/
    │   │       ├── AccountNotFoundException.java
    │   │       ├── InsufficientBalanceException.java
    │   │       ├── InvalidRequestException.java
    │   │       ├── DuplicateAccountException.java
    │   │       └── GlobalExceptionHandler.java       <- सर्व errors एका जागी
    │   └── resources/
    │       ├── application.properties                <- database आणि server settings
    │       └── static/
    │           ├── index.html                        <- web page
    │           ├── css/style.css                     <- design
    │           └── js/app.js                         <- frontend logic
    └── test/java/com/college/bank/model/
        └── AccountTest.java                          <- unit tests
```

## 5. लगेच चालवण्यासाठी (Quick Start)

```
cd bank-management-system
mvn spring-boot:run
```

मग browser मध्ये उघडा: **http://localhost:8080**

संपूर्ण माहिती `docs/01_INSTALL_AND_RUN.md` मध्ये आहे. आधी तीच वाचा.
