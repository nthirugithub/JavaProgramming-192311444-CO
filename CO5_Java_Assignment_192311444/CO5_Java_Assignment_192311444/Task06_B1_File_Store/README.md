# B1 – Instrumented File Store (7 marks)
The completed `find()` opens a BufferedReader in try-with-resources, splits each line into exactly five fields, validates route and amount, skips malformed rows and returns the matching ID. The damaged P1002 line has only two fields, so it is skipped and does not stop the scan; P1003 can still be found. Missing file returns null for find and an empty list for listByRoute.

Run:
```powershell
javac -d out src/PassStore.java src/PassStoreDemo.java src/PassStoreBenchmark.java
java -cp out PassStoreDemo
java -cp out PassStoreBenchmark
```
Growth benchmark uses 1k, 10k, 50k and 100k records; target is at the end (worst-case scan). It warms up 3 times and reports 7-run average nanoseconds. Repeat on your machine; do not copy sample numbers from another machine. For buffering compare use A2 with generated 1 MB, 10 MB, 50 MB logs and save raw outputs.
The supplied five-field TODO code is retained in field count/order. This simple exercise CSV format rejects commas in names; A1 describes proper quoted CSV for full record requirements.
