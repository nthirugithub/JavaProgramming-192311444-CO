# PROGRAMMING IN JAVA – ASSIGNMENT 5 (CO5)
## Data Persistence Engineering – Files and Databases
**Student:** N Thirrumalaa | **Register:** 192311444 | **SIMATS**  
**Department:** BE CSE – Core | **Year:** 4th Year | **JDK:** Java 25.0.1  
**GitHub repository:** Add the exact existing repository URL here after confirming the full name.

> Report draft based on the supplied CO5 assignment. Part A is design/pseudocode; Java is not a substitute for pseudocode. Part B contains implementation and measurement programs. Insert actual machine/database measurements after running them; no values are fabricated here.

# PART A – Persistence Design & Pseudocode (25 marks)

## A1 – Design a File Format (7)
### A1.1 Record layouts
**Delimited text:** passId, regNo, name, routeNo, boardingStop, validityDate, amountPaid. Use UTF-8 and CSV quoting: quote fields containing delimiter/quote/newline and double embedded quotes. A robust CSV parser is required; naive split is not sufficient.

**Binary DataOutputStream:** writeUTF(passId), writeUTF(regNo), writeUTF(name), writeInt(routeNo), writeUTF(boardingStop), writeUTF(validityDate ISO-8601), writeDouble(amountPaid). Reader must use identical order/types. `writeUTF` is length-prefixed modified UTF-8, not fixed-width.

### A1.2 SIMATS pseudocode
**AddPass**
1. START
2. VALIDATE required fields
3. OPEN BufferedWriter in CREATE + APPEND mode
4. ENCODE fields using the agreed CSV escaping
5. WRITE row and newline
6. FLUSH and CLOSE
7. IF IOException THEN report failure
8. END

**FindPass**
1. START
2. IF file missing THEN return missing-file status
3. OPEN BufferedReader
4. WHILE line exists: parse CSV; if malformed log and continue; if ID matches return record
5. CLOSE reader
6. RETURN NOT_FOUND
7. END

**ListByRoute**
1. START; CREATE result list; OPEN BufferedReader
2. FOR each line: parse and validate; if valid and route matches append; otherwise log damaged line
3. CLOSE reader; DISPLAY result
4. END

### A1.3 Damaged data
Wrong field count/type/date is rejected and logged with line number. Missing file is distinct from empty file. A power failure may leave a partial final record; detect malformed/incomplete final line and report/quarantine it. Stronger replacement uses temp file + atomic rename or journal/checksum. Flush alone is not a durability guarantee.

**Alternative RandomAccessFile:** fixed record index i starts at i×recordSize; seek is O(1) when index known. Without an ID→offset index, searching by pass ID remains O(n). Fixed-width fields waste padding space. An ID index costs additional O(n) space but supports average O(1) lookup with a hash index.

## A2 – Don't Read What You Don't Need (5)
### A2.1 Predict cost
A 50 MB file contains about 50,000,000 bytes (decimal MB) or 52,428,800 bytes (MiB). Byte-wise loop invokes InputStream.read() once per byte plus EOF. Calls are Java-level calls; BufferedInputStream reduces underlying source reads to approximately ceil(fileSize/bufferSize), though exact OS calls depend on JVM/OS/filesystem.

### A2.2 Buffered redesign
Use BufferedInputStream with a 64 KiB byte array; scan each returned block for newline. Complexity O(N) time and O(B) extra memory, where N is file size and B is buffer size.

### A2.3 Limitation
Buffering offers little benefit for tiny files, already-buffered sources, or workloads dominated by computation per byte. `Files.readAllLines()` uses O(N + line/object overhead) memory and is convenient for small files; streaming uses O(B) extra memory and is safer for large logs.

## A3 – DAO Pattern (5)
### A3.1 Common pattern
Open connection, prepare SQL, bind parameters, execute, map ResultSet to domain object, close resources. Entity/table/columns/keys/row mapping differ.

### A3.2 Generic DAO
`Dao<T,K>` defines insert, findById, findAll, update, delete. `StudentDao` implements SQL and maps rows to Student.

### A3.3 Mapping
Student.id ↔ student.student_id (PK); registerNumber ↔ register_no; name ↔ student_name; email ↔ email.

**Alternative SQL in GUI/service:** schema rename changes may require updates in multiple callers. DAO confines SQL/schema mapping to one layer. Runtime complexity of database operations is not changed by the pattern; DAO adds a small abstraction/method-call overhead.

## A4 – Serialization (4)
Mark live `Connection` transient because it is process/session state and not a serializable durable resource. Password should also be transient and never stored in plaintext snapshot. Serialization traverses reachable object graph Hostel→Rooms→Residents; non-transient reachable objects must be Serializable and shared references are tracked.

With an explicit matching serialVersionUID, a new phoneNumber field absent in an old stream gets its default value (null for String) unless custom migration logic supplies a value. Incompatible UID can cause InvalidClassException.

**CSV/JSON alternative:** more portable/readable and handles optional/new fields through parser defaults, but requires explicit mapping. Serialization conveniently preserves object graphs but couples data to Java classes. Save/load are O(n); memory depends on streaming vs materializing full graph. Text may be larger but compressible; actual size depends on encoding/metadata.

## A5 – Transactions and Injection (4)
### A5.1 Transaction pseudocode
1. START; save old autoCommit; set autoCommit false
2. UPDATE source wallet subtracting ₹500 only where balance≥500
3. IF updated row count ≠1 THEN ROLLBACK and report error
4. UPDATE destination wallet adding ₹500
5. IF updated row count ≠1 THEN ROLLBACK and report error
6. COMMIT
7. ON SQL/runtime failure ROLLBACK and rethrow
8. RESTORE old autoCommit; END

Two auto-commit statements can debit without credit if failure occurs between them. A transaction adds coordination/logging cost but protects atomic correctness.

### A5.2 Injection
Concatenating untrusted ID/password lets quote/operator/comment syntax change the WHERE predicate, potentially creating a tautology. `PreparedStatement("SELECT ... WHERE id=? AND pwd=?")` binds both as data. Escaping/regex filtering is weaker because dialect, encoding and edge cases can bypass ad-hoc filtering. Parameterization is the correct SQL boundary.

# PART B – Implementation & Experimental Investigation (25 marks)

## B1 – Instrumented File Store (7)
`PassStore.find()` reads with BufferedReader in try-with-resources, requires exactly five fields, validates route and amount, skips damaged lines and returns matching ID or null. The supplied damaged P1002 line has only two fields; it is skipped so the later P1003 record remains searchable.

Test cases: normal existing ID → five fields; absent ID → null; malformed field count → skipped; non-numeric route/amount → skipped; missing file → null/empty list. Compare implementation with A1; the B1 supplied code has five fields, while A1 full record design includes seven fields, so document that the code follows the exact provided TODO signature.

### Buffering and growth measurements
Run `LineCounter` on 1 MB, 10 MB and 50 MB logs; repeat at least five times, retain raw CSV and compare median time. Run `PassStoreBenchmark` at 1k, 10k, 50k and 100k records; target is last record (worst-case scan). Use measured values, not invented values.

| Experiment | Input sizes | Raw output location | Result |
|---|---|---|---|
| Byte-wise vs buffered | 1, 10, 50 MB | Add your CSV path | Fill after execution |
| Find growth | 1k, 10k, 50k, 100k | Add your CSV path | Fill after execution |

**Alternative HashMap:** loading n records once costs O(n) time and O(n) extra memory; q searches then average O(q), total O(n+q). Repeated scans cost O(q×n) time and O(1) extra memory (excluding returned data).

## B2 – Adversarial Input (5)
Use only a disposable local assignment database. Unsafe concatenation can accept a crafted tautology/comment input; PreparedStatement binds it literally and rejects it. The file adversarial input includes wrong field count, non-numeric amount and empty line; valid later records must still be searchable. Quote escaping/regex is weaker than binding; validation remains useful for domain rules.

## B3 – Statement vs PreparedStatement vs Batch (8)
`JdbcBenchmark` tests 10, 1,000, 10,000 and 100,000 records, prints elapsed nanoseconds and ResultSetMetaData column types. Repeat each strategy multiple times and compare medians under the same database/JVM conditions.

| Method | 10 rows | 1,000 | 10,000 | 100,000 |
|---|---:|---:|---:|---:|
| Statement | measure | measure | measure | measure |
| PreparedStatement | measure | measure | measure | measure |
| PreparedStatement batch (1,000) | measure | measure | measure | measure |

Auto-commit can commit each row separately; a transaction groups the work. Force an invalid amount halfway: the transactional loader should rollback all rows. Compare with auto-commit, where earlier rows may remain. For nightly 100k import use prepared batch + transaction; for one counter pass use parameterized single-row PreparedStatement.

Alternative multi-row INSERT/LOAD DATA may reduce round trips and improve throughput. Prepared batch memory is O(b) driver/batch state for batch size b; a multi-row SQL statement has SQL-string/parameter memory proportional to rows in statement; LOAD DATA streams file efficiently but requires server/file permissions and strict validation. Measure before choosing.

## B4 – Final Engineering Decision (5)
Migration reads file, validates rows, records rejected line number/reason, then calls transaction/batch loader. DatabaseMetaData reports product/version, driver/version and table names; this helps detect deployment differences and driver compatibility.

**Decision:** shared relational database for 12 campuses and 100× volume because it supports concurrent clients, transactions, indexes, constraints, safe parameterized queries and centralized search. Text files suit interchange/archives but are weak for concurrent writes and global queries. Serialization is Java/class-version coupled and not a multi-writer shared store.

Twelve serialized files require searching each campus file (O(total records) full scan), duplicated storage and external locking/merge coordination for concurrent counters. A database centralizes constraints and transactions but needs availability, backups, security and capacity planning.

## Final checklist
- [ ] Compile and run every Java task with local JDK 25.
- [ ] Run MySQL schema and test JDBC tasks with Connector/J.
- [ ] Run B1 file tests and buffering/growth measurements.
- [ ] Run B2 adversarial tests only on disposable local DB.
- [ ] Run B3 1k/10k/100k benchmarks and transaction failure test.
- [ ] Run B4 migration and capture DatabaseMetaData output.
- [ ] Replace measurement placeholders with actual results.
- [ ] Add exact GitHub URL and progressive commits.
