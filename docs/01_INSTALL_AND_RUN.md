# 01 - Install आणि Run कसे करायचे

## भाग 1: लागणारे software

| Software | Version | कशासाठी |
|----------|---------|---------|
| JDK (Java Development Kit) | 17 किंवा त्यापेक्षा नवीन (17 / 21) | Java program compile आणि run करायला |
| Maven | 3.8 किंवा नवीन | Dependencies download करायला आणि project build करायला |
| IDE (एक निवडा) | IntelliJ IDEA Community / VS Code / Eclipse | Code बघायला आणि लिहायला |
| Browser | Chrome / Edge / Firefox | Application उघडायला |

> **Database वेगळा install करायची गरज नाही.** आम्ही H2 वापरला आहे, तो project सोबतच येतो आणि `data/` folder मध्ये file म्हणून save होतो.

## भाग 2: JDK install करणे (Windows)

1. https://adoptium.net वर जा आणि **Temurin 17 (LTS)** चा Windows `.msi` installer download करा.
2. Installer उघडा. **"Set JAVA_HOME variable"** आणि **"Add to PATH"** हे दोन options **Will be installed on local hard drive** करा.
3. Next -> Install -> Finish.
4. **नवीन** Command Prompt उघडा (जुना नको) आणि लिहा:
   ```
   java -version
   ```
   `openjdk version "17..."` असे दिसले पाहिजे.

(Linux: `sudo apt install openjdk-17-jdk` | Mac: `brew install openjdk@17`)

## भाग 3: Maven install करणे

**सोपा मार्ग:** IntelliJ IDEA वापरत असाल तर Maven आधीच आत असतो. वेगळे install नको. (भाग 5 बघा)

**Command line साठी (Windows):**
1. https://maven.apache.org/download.cgi वरून **Binary zip archive** download करा.
2. `C:\Program Files\Apache\maven` येथे extract करा.
3. Windows Search -> **Environment Variables** -> System variables मधील `Path` निवडा -> Edit -> New -> `C:\Program Files\Apache\maven\bin` टाका.
4. नवीन Command Prompt उघडा:
   ```
   mvn -version
   ```
   Maven आणि Java ची version दिसली पाहिजे.

## भाग 4: Command line वरून project run करणे

1. Project folder `bank-management-system` तुमच्या computer वर extract करा.
2. Command Prompt / Terminal मध्ये त्या folder मध्ये जा (जिथे `pom.xml` आहे):
   ```
   cd bank-management-system
   ```
3. Run करा:
   ```
   mvn spring-boot:run
   ```
   पहिल्यांदा internet लागेल, कारण Maven सर्व libraries download करतो (5-10 मिनिटे लागू शकतात).
4. Console मध्ये खालीलसारखी ओळ दिसली की application सुरू झाले:
   ```
   Started BankManagementApplication in 4.5 seconds
   ```
5. Browser उघडून जा: **http://localhost:8080**

**Application थांबवायला:** Console मध्ये `Ctrl + C`.

### JAR file बनवून run करणे (पर्यायी)
```
mvn clean package
java -jar target/bank-management-system-1.0.0.jar
```

## भाग 5: IntelliJ IDEA मधून run करणे

1. IntelliJ उघडा -> **Open** -> `bank-management-system` folder निवडा (जिथे `pom.xml` आहे ती).
2. उजवीकडे खाली "Maven project needs to be imported" आले तर **Load Maven Changes** वर क्लिक करा. Libraries download होईपर्यंत थांबा.
3. **File -> Project Structure -> Project SDK** मध्ये **17** (किंवा 21) निवडा.
4. `src/main/java/com/college/bank/BankManagementApplication.java` उघडा.
5. `main` method च्या बाजूचा हिरवा **Run** (▶) बटन दाबा.
6. Browser मध्ये http://localhost:8080 उघडा.

## भाग 6: VS Code मधून run करणे

1. **Extension Pack for Java** आणि **Spring Boot Extension Pack** install करा.
2. Folder उघडा (File -> Open Folder).
3. `BankManagementApplication.java` उघडा आणि `main` वरील **Run** दाबा.

## भाग 7: Database बघणे (H2 Console)

Application चालू असताना: **http://localhost:8080/h2-console**

| Field | Value |
|-------|-------|
| JDBC URL | `jdbc:h2:file:./data/bankdb` |
| User Name | `sa` |
| Password | (रिकामे ठेवा) |

**Connect** दाबा. डावीकडे `ACCOUNTS` आणि `BANK_TRANSACTIONS` tables दिसतील. Query चालवून बघा:
```sql
SELECT * FROM ACCOUNTS;
SELECT * FROM BANK_TRANSACTIONS;
```

## भाग 8: Database मधला सर्व data पुसणे (नवीन सुरुवात)

Application **बंद करा** आणि project मधील `data` folder delete करा. पुढच्या run ला नवीन रिकामा database बनेल.

## भाग 9: MySQL वापरायचे असेल तर (पर्यायी)

1. MySQL install करा आणि चालू ठेवा.
2. `pom.xml` मध्ये `<dependencies>` आत हे add करा:
   ```xml
   <dependency>
       <groupId>com.mysql</groupId>
       <artifactId>mysql-connector-j</artifactId>
       <scope>runtime</scope>
   </dependency>
   ```
3. `application.properties` मध्ये H2 च्या 4 datasource lines comment करा आणि खालच्या MySQL lines uncomment करून password बदला.
4. Application पुन्हा run करा. `bankdb` database आणि tables आपोआप बनतील.

## भाग 10: Unit tests चालवणे

```
mvn test
```
`Tests run: 6, Failures: 0, Errors: 0` दिसले की सर्व tests पास.
