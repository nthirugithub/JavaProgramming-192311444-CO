# Java Programming Assignment 4 – CO4
**Student:** N Thirrumalaa | **Register:** 192311444 | **SIMATS**  
**Department:** BE CSE – Core | **Year:** 4th Year | **JDK:** Java 25.0.1  
**Repository name:** `CSA09-192311444-Assignment4`

This project follows the supplied CO4 assignment: AWT/Swing, event delegation, listeners, adapters, inner/anonymous classes, layout managers, menus/dialogs and Swing–JDBC integration. Task 6 uses **MySQL + JDBC**, not SQLite.

## Folders
- Task01_GUI_Design_Review – FeeWindow and FeeService
- Task02_Hospital_Appointment – event table, component tree and pseudocode
- Task03_Library_Search – resizable Swing layout
- Task04_Drawing_Pad – corrected mouse handling and repainting
- Task05_Currency_Converter – four listener styles
- Task06_Event_Registration – layered Swing/MySQL JDBC application and SQL schema
- Report – assignment analysis and test plan
- Evidence – instructions for actual screenshots

## Compile/run (PowerShell, from this folder)
```powershell
javac -d Task01_GUI_Design_Review/out Task01_GUI_Design_Review/src/*.java
java -cp Task01_GUI_Design_Review/out FeeWindow

javac -d Task03_Library_Search/out Task03_Library_Search/src/*.java
java -cp Task03_Library_Search/out LibrarySearchApp

javac -d Task04_Drawing_Pad/out Task04_Drawing_Pad/src/*.java
java -cp Task04_Drawing_Pad/out DrawPad

javac -d Task05_Currency_Converter/out Task05_Currency_Converter/src/*.java
java -cp Task05_Currency_Converter/out ConverterDemo
```

## Task 6 setup
1. Install/start MySQL Server 8.x.
2. Execute `Task06_Event_Registration/database/schema.sql`.
3. Set `$env:DB_URL="jdbc:mysql://localhost:3306/campus_events"`, `$env:DB_USER="root"` and `$env:DB_PASSWORD="your_password"` in PowerShell.
4. Download MySQL Connector/J 9.x and place its JAR in `Task06_Event_Registration/lib/`.
5. Compile:
```powershell
$src = Get-ChildItem Task06_Event_Registration/src -Recurse -Filter *.java | ForEach-Object FullName
javac -cp "Task06_Event_Registration/lib/mysql-connector-j.jar" -d Task06_Event_Registration/out $src
java -cp "Task06_Event_Registration/out;Task06_Event_Registration/lib/mysql-connector-j.jar" app.Main
```
DB Browser for SQLite is not a MySQL server. It cannot replace MySQL for this task.

## Evidence and GitHub
The ZIP contains source, test plans and sample seed records, not fabricated execution screenshots or measured results. Capture your own program screenshots in `Evidence/Actual/`, with captions linking each image to a requirement. Create the GitHub repository above and commit progressively, at least one commit per task.
