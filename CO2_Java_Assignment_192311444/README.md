# CO2 – Programming in Java Assignment 2

**Student:** N Thirrumalaa  
**Register Number:** 192311444  
**Institution:** SIMATS  
**Department:** BE CSE – Core  
**Year:** IV  
**Environment:** Windows 11, VS Code, Java 25.0.1 LTS

## Repository structure
- `Task01_StringPerformance` – String, StringBuilder and StringBuffer benchmark
- `Task02_ListComparison` – ArrayList, LinkedList and ArrayDeque
- `Task03_HashSetEquality` – equals/hashCode and HashSet de-duplication
- `Task04_MapComparison` – HashMap, TreeMap and LinkedHashMap
- `Task05_GenericLeaderboard` – generics and comparator-based ranking
- `Task06_PriorityQueueTriage` – priority and arrival-order queue
- `Task07_DesignChallenge` – diagnosis and validated StringBuilder redesign
- `Experimental_Data` – save CSV output from benchmark programs here

## Compile and run (PowerShell)
Open a terminal in this folder. Compile each task independently:

```powershell
javac Task01_StringPerformance\StringPerformance.java
java -cp Task01_StringPerformance StringPerformance

javac Task02_ListComparison\ListComparison.java
java -cp Task02_ListComparison ListComparison

javac Task03_HashSetEquality\Attendance.java
java -cp Task03_HashSetEquality Attendance

javac Task04_MapComparison\PharmacyMaps.java
java -cp Task04_MapComparison PharmacyMaps

javac Task05_GenericLeaderboard\LeaderboardDemo.java
java -cp Task05_GenericLeaderboard LeaderboardDemo

javac Task06_PriorityQueueTriage\Triage.java
java -cp Task06_PriorityQueueTriage Triage

javac Task07_DesignChallenge\StringRedesign.java
java -cp Task07_DesignChallenge StringRedesign
```

Run from the project root. The benchmark programs print CSV-shaped measurements. Copy their output into `Experimental_Data` and preserve the original terminal output/screenshots.

## Important experimental note
Timing values depend on the laptop, JVM warm-up, background processes and runtime conditions. Do not copy sample timing values from another machine. Run each benchmark at least five times where applicable and report the mean. The source code includes repeated runs for Q1; for other benchmarks, repeat the program execution and retain each run's output.

## GitHub
Create/use repository: `CSA09-192311444-Assignment2`. Add the faculty as collaborator if private. Commit each task separately; do not upload everything as one final commit.

## Verification
Expected key outputs:
- Q3 corrected `HashSet` count is 2.
- Q5 ranking is `[Eagles(9,6), Bisons(9,4), Panthers(9,4), Cobras(6,1)]`.
- Q6 treatment order is `Meena(S1) Kumar(S1) Lata(S2) Ravi(S3) John(S3) Asha(S5)`.

Check all files compile with `javac -version` before submission.

## Sample outputs and evidence
`Experimental_Data/*_sample.txt` and `Sample_Output_Screenshots/` contain clearly labelled simulated examples to show the expected presentation format. They are not actual measurements or screenshots from the student's machine. Replace them with genuine outputs from local runs before submission.
