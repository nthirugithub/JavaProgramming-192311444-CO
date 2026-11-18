# Programming in Java – Assignment 5 (CO5)
**Student:** N Thirrumalaa  
**Register Number:** 192311444  
**College:** SIMATS | **Department:** BE CSE – Core | **Year:** 4th Year  
**JDK:** Java 25.0.1 | **IDE:** VS Code

**Assignment:** Data Persistence Engineering – Files and Databases  
**GitHub:** Use your existing `JavaProgramming-192311444-CO...` repository. The screenshot only showed a truncated repository name; replace the URL placeholder below with the exact repository URL before submission.

## Project structure
- `Task01_A1_File_Format/` – delimited/binary record layouts, pseudocode and failure handling.
- `Task02_A2_Buffered_IO/` – byte-wise vs buffered line counting.
- `Task03_A3_DAO_Pattern/` – generic DAO interface and StudentDao.
- `Task04_A4_Serialization/` – serializable Hostel object graph and transient fields.
- `Task05_A5_Transactions_Injection/` – JDBC transaction and safe login.
- `Task06_B1_File_Store/` – PassStore TODO, damaged-line handling, benchmarks.
- `Task07_B2_Adversarial_Input/` – unsafe vs prepared login and malformed pass inputs.
- `Task08_B3_JDBC_Benchmarks/` – Statement, PreparedStatement and batch loading.
- `Task09_B4_Migration/` – file-to-MySQL migration and DatabaseMetaData.
- `Report/` – Part A pseudocode, analysis, test plans and decision report.
- `database/schema.sql` – MySQL schema for all JDBC tasks.

## Database requirement
The supplied CO5 specification requires **MySQL + JDBC**. DB Browser for SQLite is not a MySQL server. Use MySQL Server 8.x and MySQL Connector/J 9.x.

Set environment variables in PowerShell:
```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/co5_bus_pass"
$env:DB_USER="root"
$env:DB_PASSWORD="your_password"
```
Place the MySQL Connector/J JAR at `lib/mysql-connector-j.jar` in the project root (create `lib` if needed). Run `database/schema.sql` in MySQL Workbench or mysql client first.

## Compile and run
Run from the project root. Examples:
```powershell
javac -d out/Task06 Task06_B1_File_Store/src/*.java
java -cp out/Task06 PassStoreDemo

javac -d out/Task02 Task02_A2_Buffered_IO/src/*.java
java -cp out/Task02 LineCounter

$src = Get-ChildItem Task08_B3_JDBC_Benchmarks/src -Recurse -Filter *.java | ForEach-Object FullName
javac -cp "lib/mysql-connector-j.jar" -d out/Task08 $src
java -cp "out/Task08;lib/mysql-connector-j.jar" jdbcbench.JdbcBenchmark
```
Other JDBC task run commands are included in each task README. Use the Windows classpath separator `;`.

## Measurement and evidence integrity
The benchmark programs generate measurements on the machine/JDK/database where they are run. No fabricated benchmark values or execution screenshots are included. Store your actual CSV output under `results/` and add your measured interpretation to the report. This project intentionally has no separate Evidence/Screenshots folder.

## GitHub submission
The assignment PDF requests a repository named `CSA09-192311444-Assignment5`, but you have an existing JavaProgramming repository. Confirm with faculty whether adding this assignment to your existing repository is acceptable. Do not invent a GitHub URL. Commit progressively, at least one commit per task, as required by the PDF.
