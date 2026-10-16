# 06 - PPT Outline (12 Slides)

> प्रत्येक slide वर कमी text, जास्त diagram/screenshot ठेवा. खाली "बोलायचे" म्हणजे तोंडी सांगायचे मुद्दे.

---

### Slide 1 - Title
- **Bank Management System**
- Java | Spring Boot | HTML | CSS | JavaScript
- Team members, Roll numbers, Guide name, College
- *बोलायचे:* नमस्कार, आम्ही ... आमचा project सादर करत आहोत.

### Slide 2 - Problem Statement आणि Objectives
- कागदी बँक व्यवहार: वेळखाऊ, चुका होतात
- उद्दिष्ट: computerized system + OOP चा प्रत्यक्ष वापर
- 6 मुख्य सुविधा
- *बोलायचे:* आम्हाला OOP शिकायचे होते, म्हणून बँकेसारखे वास्तविक उदाहरण निवडले.

### Slide 3 - Features
- Create Account (Savings / Current)
- Deposit | Withdraw | Check Balance
- Search Account | Transaction History
- Validation आणि Error handling
- (6 icons किंवा 6 छोटे boxes)

### Slide 4 - Technology Stack
- Table: Frontend (HTML/CSS/JS) | Backend (Java 17, Spring Boot) | Database (H2) | Tools (Maven, IntelliJ, Git)
- *बोलायचे:* H2 वापरला कारण वेगळे install करावे लागत नाही; MySQL ला बदलता येतो.

### Slide 5 - System Architecture
- Layer diagram: Browser -> Controller -> Service -> Repository -> Database
- प्रत्येक layer चे एका ओळीत काम
- *बोलायचे:* प्रत्येक layer ची एकच जबाबदारी, त्यामुळे code सांभाळणे सोपे.

### Slide 6 - OOP Class Diagram
- Transactable (interface) -> Account (abstract) -> SavingsAccount, CurrentAccount
- (Report मधला class diagram वापरा)

### Slide 7 - OOP Concepts (सर्वात महत्त्वाची slide)
- 6 concepts + प्रत्येकाचे एक उदाहरण (README मधला table)
- *बोलायचे:* Encapsulation: balance थेट बदलता येत नाही. Polymorphism: Savings आणि Current साठी minimum balance वेगळा.

### Slide 8 - Database Design
- `accounts` आणि `bank_transactions` tables
- Single Table Inheritance: दोन्ही प्रकारचे accounts एका table मध्ये

### Slide 9 - REST API आणि Request Flow
- API table (7 URLs)
- Withdraw चा flow diagram (पाऊले 1-6)

### Slide 10 - Validation आणि Error Handling
- Frontend + Backend दोन्ही
- 3 उदाहरणे: चुकीचा phone, minimum balance, duplicate email
- Error message चा screenshot

### Slide 11 - Live Demo (किंवा Screenshots)
क्रम: Create Account -> Deposit -> Withdraw (success) -> Withdraw (error) -> Check Balance -> Search -> History
- *टीप:* Demo आधी application चालू ठेवा; बॅकअप म्हणून screenshots तयार ठेवा.

### Slide 12 - Testing, Limitations, Future Scope, Conclusion
- Testing: 6 unit tests + 22 manual test cases
- Limitations: Login नाही, Fund Transfer नाही
- Future: Spring Security, Fund Transfer, PDF statement
- **Thank you / Questions?**

---

## Presentation वाटणी

| Slides | कोण बोलणार |
|--------|------------|
| 1-4 | Member 3 |
| 5-8 | Member 1 (OOP आणि Database) |
| 9-10 | Member 2 |
| 11 (Demo) | तिघे मिळून (Member 3 चालवतो, बाकी स्पष्ट करतात) |
| 12 | Member 2 आणि 3 |
