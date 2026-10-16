# 08 - OOPCG Subject साठी Notes

## 1. Madam ला काय सांगायचे

> "Madam, आमचा project Bank Management System आहे. **Backend पूर्णपणे OOP वर आधारित आहे** (Class, Object, Encapsulation, Inheritance, Abstraction, Polymorphism, Interface, Exception Handling). **Computer Graphics चा भाग** म्हणून आम्ही Bar chart, Pie chart आणि Line graph कोणतीही library न वापरता HTML5 Canvas वर स्वतः काढले आहेत."

## 2. Backend मध्ये OOP कुठे आहे (दाखवण्यासाठी file आणि line)

| OOP Concept | File | काय दाखवायचे |
|-------------|------|--------------|
| Class / Object | `model/Account.java`, `service/AccountService.java` | `new SavingsAccount(...)` ने object बनतो |
| Encapsulation | `model/Account.java` | `private` fields, `setBalance()` नाही |
| Constructor | `Account.java`, `SavingsAccount.java` | `super(...)` ने parent constructor बोलावला |
| Inheritance | `SavingsAccount.java`, `CurrentAccount.java` | `extends Account` |
| Abstraction | `Account.java`, `Transactable.java` | `abstract class`, `abstract methods`, `interface` |
| Polymorphism (overriding) | `SavingsAccount.java`, `CurrentAccount.java` | `getMinimumBalance()` आणि `calculateMonthlyInterest()` |
| Runtime polymorphism | `AccountService.createAccount()` | `Account account = new SavingsAccount(...)` (parent reference, child object) |
| Interface | `Transactable.java` | `Account implements Transactable` |
| Exception Handling | `exception/` package | Custom exceptions + `GlobalExceptionHandler` |
| Static / final | `SavingsAccount.java` | `private static final BigDecimal MINIMUM_BALANCE` |
| Access specifiers | सर्व classes | `private`, `protected`, `public` |
| Collections | `AccountService.java` | `List<Account>`, `stream().map()` |

## 3. Computer Graphics चा भाग (Menu: "Graphs (Computer Graphics)")

File: `src/main/resources/static/js/app.js` (भाग "7. Graphs")

| Graph | Function | काय काढले जाते |
|-------|----------|-----------------|
| Bar chart | `drawBarChart()` | Accounts चा balance (positive teal, negative लाल) |
| Pie chart | `drawPieChart()` | Savings आणि Current accounts चे प्रमाण (%) |
| Line graph | `drawLineChart()` | एका account चा प्रत्येक व्यवहारानंतरचा balance (Deposit हिरवा, Withdraw लाल बिंदू) |

### वापरलेल्या Computer Graphics संकल्पना

| संकल्पना | Code मध्ये |
|----------|-----------|
| **Coordinate system** | Canvas चा (0,0) बिंदू वर-डाव्या कोपऱ्यात; y खाली वाढतो |
| **Scaling / Mapping** | `toY = top + (max - value) / (max - min) * height` - value ला screen चा y बनवतो |
| **Line drawing primitive** | `moveTo()`, `lineTo()`, `stroke()` - axes, grid, line graph |
| **Rectangle primitive** | `fillRect()` - bars |
| **Arc / Circle primitive** | `arc()` - pie slices आणि line graph चे बिंदू |
| **Angle calculation** | `angle = value / total * 2 * PI` (pie) |
| **Trigonometry** | `cos(mid)`, `sin(mid)` - slice च्या मध्यावर % text ठेवायला |
| **Color model** | Hex (RGB) रंग, fill आणि stroke |
| **Line style** | `lineWidth`, `setLineDash([4,4])` (dashed grid) |
| **Text rendering** | `fillText()` |
| **Clipping/Clearing** | `clearRect()` - graph पुन्हा काढताना जुने पुसणे |

> **Graph कसा बघायचा:** काही accounts बनवा, Deposit/Withdraw करा, मग menu मधून "Graphs" उघडा. Line graph साठी account number टाकून "Draw graph" दाबा.

## 4. OOPCG Viva - अतिरिक्त प्रश्न

**1. Computer Graphics म्हणजे काय?**
Computer वापरून चित्रे, आकृत्या आणि graphs तयार करणे आणि screen वर दाखवणे.

**2. Raster आणि Vector graphics मध्ये फरक?**
Raster: pixels चे बनलेले (उदा. JPG, PNG). मोठे केले की धूसर. Vector: गणिती सूत्रांवर आधारित (रेषा, वक्र); मोठे केले तरी स्पष्ट. आम्ही Canvas वर काढतो, ते pixel based (raster) आहे.

**3. Canvas मध्ये coordinate system कसा असतो?**
(0,0) वर-डाव्या कोपऱ्यात; x उजवीकडे वाढतो, y खाली वाढतो. म्हणून मोठी value काढायला y कमी करावा लागतो; आमचे `toY()` हेच करते.

**4. तुम्ही pie chart चा कोन कसा काढला?**
`कोन = (त्या भागाची संख्या / एकूण संख्या) x 2 x PI` (radians मध्ये). पूर्ण वर्तुळ = 2 x PI radians = 360 अंश.

**5. Line काढण्याचे algorithm कोणते माहीत आहेत?**
DDA आणि Bresenham's line algorithm. Canvas मध्ये `lineTo()` हे आपल्यासाठी करते; आतून तेच algorithm वापरले जातात.

**6. Scaling म्हणजे काय?**
वस्तूची size बदलणे. आमच्या graph मध्ये मोठ्या रकमा (उदा. 1,00,000) लहान pixel जागेत बसवायला value चे scaling केले आहे.

**7. Graph मध्ये negative balance कसा दाखवला?**
Zero line (baseline) निश्चित केली, positive bars वर आणि negative bars खाली, आणि negative bar लाल रंगात.

**8. Library (उदा. Chart.js) का नाही वापरली?**
Subject Computer Graphics चा आहे, म्हणून मूळ primitives (line, rectangle, arc) स्वतः वापरून समजून घ्यायचे होते.

## 5. जर madam ने C++ मध्ये project मागितला तर?

आत्ताचा project Java मध्ये आहे आणि madam ने "काहीही वापरा, backend ला OOP हवे" असे सांगितले आहे, त्यामुळे चालतो. पण जर त्या C++ (`graphics.h` / OpenGL) मध्ये मागत असतील तर मला सांगा; तीच OOP रचना (Account -> SavingsAccount / CurrentAccount) C++ मध्ये बनवून देता येईल.
