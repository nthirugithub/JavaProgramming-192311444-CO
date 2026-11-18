# A2 – Don't Read What You Don't Need (5 marks)
A 50 MB file contains approximately 52,428,800 bytes if MB means MiB, or 50,000,000 bytes if decimal MB. The byte-wise loop makes one Java `read()` call per byte (plus the final EOF read). Depending on the stream implementation, these calls may not each become an OS system call, but they still incur method/loop overhead. Buffering reads blocks (here 64 KiB) and reduces underlying read operations to roughly fileSize/bufferSize, plus a final read.

A buffer helps less when the data source is already buffered, the file is tiny, or the program performs expensive work per byte that dominates I/O. `Files.readAllLines()` needs O(file size + line/object overhead) memory and is suitable only when the whole file comfortably fits memory; streaming uses O(buffer size) extra memory.

Run:
```powershell
javac -d out src/LineCounter.java
java -cp out LineCounter path\to\log.txt
```
The program prints CSV-like timing rows. Run at least 5 repetitions for 1 MB, 10 MB and 50 MB inputs, report medians and retain raw outputs. Timings are machine-dependent.
