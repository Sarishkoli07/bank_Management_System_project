# 07 - Viva Questions आणि Answers

## भाग A: OOP (प्रश्न 1-12)

**1. Class म्हणजे काय? Object म्हणजे काय?**
Class म्हणजे नकाशा / साचा (blueprint). Object म्हणजे त्या साच्यातून बनलेली खरी वस्तू. आमच्या project मध्ये `Account` हा class आहे आणि `new SavingsAccount(...)` ने बनवलेला प्रत्येक customer चा account हा object आहे.

**2. Encapsulation म्हणजे काय? तुमच्या project मध्ये कुठे आहे?**
Data (fields) आणि त्यावर चालणारे methods एकत्र बांधणे आणि data बाहेरून थेट बदलता न येणे. `Account` मध्ये सर्व fields `private` आहेत आणि `setBalance()` दिलेला नाही. Balance फक्त `deposit()` आणि `withdraw()` ने बदलतो, ज्यात आम्ही नियम तपासतो.

**3. Inheritance म्हणजे काय? उदाहरण द्या.**
एका class ने दुसऱ्या class चे गुणधर्म आणि methods घेणे (`extends`). `SavingsAccount extends Account` - त्याला `accountNumber`, `balance`, `deposit()` वगैरे पुन्हा लिहावे लागत नाहीत. फायदा: code पुन्हा वापरता येतो (reusability).

**4. Abstraction म्हणजे काय? ते तुम्ही कसे साध्य केले?**
गरजेची माहिती दाखवणे आणि अनावश्यक तपशील लपवणे. आम्ही `abstract class Account` आणि `interface Transactable` वापरले. त्यात "काय करायचे" (deposit, withdraw, minimum balance) ठरवले आहे; "कसे" हे child class ठरवतो.

**5. Polymorphism म्हणजे काय? तुमच्या project मध्ये कुठे?**
एकच नाव/एकच code, पण वस्तूनुसार वेगळे वर्तन. `Account.withdraw()` मध्ये `getMinimumBalance()` बोलावतो; Savings object असेल तर 500 येतो आणि Current object असेल तर -5000. हे **method overriding (runtime polymorphism)** आहे.

**6. Abstract class आणि Interface मध्ये फरक काय?**
| Abstract class | Interface |
|---|---|
| Fields, constructor, normal methods असू शकतात | मुख्यतः फक्त method declarations (Java 8 पासून default methods असतात) |
| एक class फक्त एकच abstract class extend करू शकतो | एक class अनेक interfaces implement करू शकतो |
| "is-a" संबंध (Savings **is an** Account) | "can-do" क्षमता (Account **can** transact) |

**7. Abstract class चा object बनवता येतो का? का नाही?**
नाही. `new Account()` केले तर compile error येतो, कारण त्यातले काही methods अपूर्ण (abstract) आहेत. आम्हाला Savings किंवा Current हा ठराविक प्रकारच हवा.

**8. Method Overloading आणि Overriding मध्ये फरक?**
Overloading: एकाच class मध्ये एकच नाव, वेगळे parameters (compile time). Overriding: child class parent चा method पुन्हा लिहितो, नाव आणि parameters तेच (runtime). आमच्याकडे overriding आहे: `getMinimumBalance()`.

**9. `super` keyword कशासाठी वापरला?**
Child च्या constructor मधून parent चा constructor बोलावण्यासाठी: `super(accountNumber, holderName, ...)`.

**10. `private`, `protected`, `public` मध्ये फरक?**
`private` - फक्त त्याच class मध्ये. `protected` - त्याच package आणि child classes मध्ये. `public` - सर्वत्र. आम्ही Account चे constructor `protected` ठेवले, म्हणजे फक्त child classes ते वापरू शकतात.

**11. `@Override` annotation चे काम काय?**
Compiler ला सांगते की हा method parent चा method override करतो. स्पेलिंग चुकली तर compiler लगेच error दाखवतो.

**12. Custom Exception का बनवले?**
योग्य आणि स्पष्ट message देण्यासाठी (उदा. `InsufficientBalanceException`) आणि प्रत्येक प्रकारच्या चुकीला वेगळा HTTP status (404, 400, 409) देण्यासाठी.

---

## भाग B: Spring Boot, Database, Validation (प्रश्न 13-24)

**13. Spring Boot म्हणजे काय? फायदा काय?**
Spring Framework वर आधारित tool जे कमीत कमी configuration मध्ये application बनवतो. आत Tomcat server असतो, त्यामुळे वेगळे server install करावे लागत नाही; `main()` चालवला की application सुरू होतो.

**14. REST API म्हणजे काय?**
HTTP वापरून (GET, POST...) client आणि server यांनी data (सहसा JSON) ची देवाणघेवाण करण्याची पद्धत. आमची frontend JavaScript याच API ला बोलावते.

**15. Layered Architecture म्हणजे काय? तुमचे layers कोणते?**
Controller (request घेतो) -> Service (नियम) -> Repository (database) -> Database. प्रत्येक layer चे एकच काम; त्यामुळे code सांभाळणे आणि तपासणे सोपे.

**16. Dependency Injection म्हणजे काय?**
गरज असलेले objects आपण `new` ने बनवत नाही; Spring ते आपोआप बनवून देतो. उदा. `AccountService` च्या constructor मध्ये `AccountRepository` दिले, Spring ते जोडतो.

**17. JPA आणि Hibernate म्हणजे काय?**
JPA हे Java objects आणि database tables जोडण्याचे standard (ORM) आहे; Hibernate ही त्याची अंमलबजावणी. आपण SQL न लिहिता `save()`, `findAll()` वापरतो.

**18. `@Entity`, `@Id`, `@Column` चे अर्थ?**
`@Entity` - class ही database table आहे. `@Id` - Primary Key. `@Column` - column ची settings (नाव, null चालेल का, लांबी).

**19. Single Table Inheritance म्हणजे काय?**
Parent आणि सर्व child classes चा data एकाच table मध्ये, आणि `account_type` (discriminator) column सांगतो की ती row Savings आहे का Current.

**20. DTO म्हणजे काय आणि Entity थेट का पाठवत नाही?**
DTO = Data Transfer Object. त्यात फक्त हवा तेवढाच data असतो. Entity थेट पाठवली तर database ची रचना उघड होते आणि नको असलेले fields बाहेर जाऊ शकतात.

**21. `@Valid` आणि Validation कसे काम करते?**
DTO मधल्या annotations (`@NotBlank`, `@Pattern`...) नुसार Spring request तपासतो. नियम मोडला तर `MethodArgumentNotValidException` येतो आणि आमचा `GlobalExceptionHandler` 400 + message पाठवतो.

**22. `@Transactional` म्हणजे काय? बँकेत का महत्त्वाचे?**
Method मधील सर्व database बदल एकत्र एक व्यवहार (transaction) मानले जातात. मधेच error आला तर सर्व बदल रद्द (rollback). म्हणजे "balance कमी झाला पण transaction record save झाला नाही" असे होत नाही.

**23. पैशांसाठी `double` ऐवजी `BigDecimal` का?**
`double` मध्ये rounding चुका होतात (0.1 + 0.2 = 0.30000000000000004). `BigDecimal` अचूक असतो, म्हणून पैशांसाठी तोच वापरतात.

**24. H2 आणि MySQL मध्ये काय फरक? तुम्ही H2 का वापरला?**
H2 हा Java मध्ये बनलेला हलका database आहे जो project सोबत येतो, install नको. MySQL मोठा, वेगळा server असलेला आणि प्रत्यक्ष कामात वापरला जाणारा database आहे. शिकण्यासाठी H2 सोपा; `application.properties` बदलून MySQL वर जाता येते (सूचना file मध्ये आहेत).

---

## भाग C: Frontend, Testing, इतर (प्रश्न 25-30)

**25. Frontend backend शी कसे बोलते?**
JavaScript च्या `fetch()` ने API URL ला request पाठवते (`POST /api/accounts/{no}/deposit`). Response JSON म्हणून येतो आणि तो आपण page वर दाखवतो. `async/await` वापरले आहे म्हणजे उत्तराची वाट बघताना page अडकत नाही.

**26. Validation frontend आणि backend दोन्ही ठिकाणी का?**
Frontend validation user ला लगेच सांगते (सोय). पण ते सुरक्षा नाही, कारण कोणीही Postman वगैरेने थेट server ला चुकीचा data पाठवू शकतो. म्हणून खरे आणि अंतिम validation backend मध्ये.

**27. XSS म्हणजे काय? तुम्ही कसे थांबवले?**
Attacker ने नावाच्या जागी `<script>...</script>` टाकून तो इतरांच्या browser मध्ये चालवणे. आमच्या `esc()` function ने user चा text HTML म्हणून नाही तर साधा text म्हणून दाखवतो.

**28. तुम्ही project कसा test केला?**
JUnit ने 6 unit tests (OOP classes), आणि Browser/Postman ने 22 manual test cases (चांगले आणि चुकीचे input, minimum balance, duplicate email, नसलेला account).

**29. Spring Boot application कसा सुरू होतो?**
`main()` मधील `SpringApplication.run()` application context बनवतो, `@Component/@Service/@Repository/@RestController` classes शोधून त्यांचे objects बनवतो, Tomcat सुरू करतो आणि port 8080 वर ऐकायला लागतो.

**30. तुमच्या project च्या मर्यादा काय आणि पुढे काय जोडाल?**
Login नाही, Fund Transfer नाही, एकाच वेळी दोन व्यवहारांचे (concurrency) संरक्षण नाही. पुढे: Spring Security, Fund Transfer, PDF statement, `@Version` ने optimistic locking, MySQL, cloud deploy.

---

## बोनस प्रश्न (तयारीसाठी)

**B1. Minimum balance च्या नियमाचा कोड कुठे आहे?**
`Account.withdraw()` मध्ये `newBalance.compareTo(getMinimumBalance()) < 0` तपासले जाते.

**B2. Account number कसा बनतो?**
`AccountService.generateAccountNumber()` - 10 अंकी random number, आणि database मध्ये आधीच नाही याची खात्री होईपर्यंत लूप.

**B3. Current account मध्ये balance minus का जाऊ शकतो?**
Overdraft सुविधा - Rs. 5000 पर्यंत; `CurrentAccount.getMinimumBalance()` -5000 देतो.

**B4. Password / Login का नाही?**
Mini project च्या वेळेत OOP आणि Spring Boot चा मूळ आधार शिकण्यावर भर दिला; Spring Security हे Future Scope मध्ये आहे.

**B5. `static` folder चे काय काम?**
Spring Boot या folder मधल्या HTML, CSS, JS files थेट browser ला दाखवतो.

**B6. `ddl-auto=update` म्हणजे?**
Entity classes बघून tables आपोआप बनवतो किंवा बदलतो, आणि आधीचा data जपतो.

**B7. HTTP status codes जे तुम्ही वापरले?**
200 OK, 201 Created (Account तयार), 400 Bad Request (चुकीचा input), 404 Not Found (account नाही), 405 Method Not Allowed, 409 Conflict (duplicate email), 500 Server Error.

**B8. Controller मध्ये business logic का नको?**
Controller फक्त request घेतो आणि परत देतो. नियम Service मध्ये असतात म्हणजे ते पुन्हा वापरता येतात आणि तपासायला सोपे.
