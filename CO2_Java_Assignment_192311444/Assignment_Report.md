# PROGRAMMING IN JAVA – ASSIGNMENT 2 (CO2)

**Student Name:** N Thirrumalaa  
**Register Number:** 192311444  
**College:** SIMATS  
**Department:** BE CSE – Core  
**Year:** IV  
**Java Version:** Java 25.0.1 LTS  
**IDE:** Visual Studio Code  
**GitHub Repository:** https://github.com/nthirugithub/JavaProgramming-192311444-CO

## Declaration of experimental method
All benchmark values in this report must be collected on the student's own laptop. The programs in this submission print raw timing measurements. JVM warm-up, garbage collection, background applications and CPU frequency scaling affect timings. Therefore, run each benchmark several times under similar conditions, record the raw output, and calculate the mean. No machine-specific timing result is fabricated in this report.

---

 
> **Evidence label:** Values marked with `*` and files named `*_sample.txt` are simulated illustrative examples, not measurements from the student's laptop. They demonstrate how to present and interpret results. Replace them with actual repeated runs before final submission. The assignment requires the student's own experimental evidence.

# Q1 – Building Text Under Load: String, StringBuilder or StringBuffer (6 marks)

## Prediction
`String` is immutable. Each concatenation creates a new result string, copying the previous content and the appended content. Repeated concatenation in a loop therefore performs increasing copying work. `StringBuilder` is mutable and appends into a resizable character buffer, normally giving linear total work in the final output length. `StringBuffer` has similar append behaviour but synchronizes its methods, adding synchronization overhead.

## Workloads
1. Small ticket: 6 fields, 1 ticket.
2. Large booking load: 50 fields, 10,000 tickets.
3. Additional workload: 200 fields, 2,000 tickets.

`StringPerformance.java` runs the three approaches and reports nanoseconds averaged over five measured repetitions after warm-up. The returned string length is consumed to reduce dead-code elimination risk.

## Measurement table
Paste the three workload rows from the program output here after running it on the submission laptop.

| Workload | Fields | Tickets | String mean (ns) | StringBuilder mean (ns) | StringBuffer mean (ns) |
|---|---:|---:|---:|---:|---:|
| Small | 6 | 1 | 18,400* | 9,200* | 11,600* |
| Large | 50 | 10,000 | 482,000,000* | 71,400,000* | 96,300,000* |
| Additional | 200 | 2,000 | 391,000,000* | 36,200,000* | 51,800,000* |

## Object and memory discussion
The number of temporary strings created by `+` inside the loop grows with the number of concatenations; the exact allocation count depends on compiler/JVM optimisations. Builder and Buffer reuse an internal mutable buffer during append and create the final immutable String at `toString()`. Heap-memory readings from a single before/after sample are noisy and include unrelated JVM activity; repeated measurements or a profiler are preferable.

## Decision
Use a local `StringBuilder` for each ticket because the builder is not shared between threads and avoids synchronization overhead. For a shared booking log accessed concurrently by multiple threads, use `StringBuffer` if a single synchronized mutable buffer is genuinely required, or preferably use a thread-safe logging queue/framework. Do not share a per-ticket builder across threads.

## Alternative concepts and complexity
For n fields and total output length L, repeated concatenation can copy a growing prefix on every iteration; under similarly sized fields this is commonly described as O(n²) copied characters. StringBuilder appends are amortized O(L), often simplified to O(n) when field sizes are bounded. `String.join`/`StringJoiner` are useful for joining already available values and delimiters; `String.format` is convenient for formatted output but formatting machinery can add overhead. Their actual performance should be measured rather than assumed.

---

# Q2 – ArrayList, LinkedList and ArrayDeque (4 marks)

## Design and predictions
`ArrayList` provides amortized O(1) append and O(1) indexed access. Inserting/removing at the front or middle requires shifting elements and is O(n). `LinkedList` has O(1) insertion/removal when the node position is already known, but locating an index is O(n), and each element requires node links. `ArrayDeque` is a resizable circular array suited to queue/deque operations at both ends, amortized O(1), but it does not provide indexed access.

## Experiment
`ListComparison.java` tests sizes 1,000, 10,000 and 50,000. It records indexed access, full iteration and a batch of front insertions. Run it at least five times and retain each output. The program also demonstrates an Up Next deque.

| Operation | ArrayList | LinkedList | ArrayDeque |
|---|---|---|---|
| Add at end | Amortized O(1) | O(1) | Amortized O(1) |
| Insert at front | O(n) | O(1) | O(1) |
| Insert in middle by index | O(n) | O(n) to locate; O(1) link once located | Not supported by index |
| Get k-th | O(1) | O(n) | Not supported by index |
| Full iteration | O(n) | O(n) | O(n) |
| Remove front | O(n) | O(1) | O(1) |
| Space per element | Reference slot; spare capacity possible | Node plus links and object overhead | Reference slot; spare capacity possible |

## Illustrative benchmark snapshot (simulated; nanoseconds)
| Size | ArrayList get | LinkedList get | ArrayList iterate | LinkedList iterate | ArrayList front batch | LinkedList front batch |
|---:|---:|---:|---:|---:|---:|---:|
| 1,000 | 48,000* | 1,720,000* | 39,000* | 52,000* | 620,000* | 85,000* |
| 10,000 | 51,000* | 16,400,000* | 410,000* | 530,000* | 8,200,000* | 190,000* |
| 50,000 | 53,000* | 81,100,000* | 2,050,000* | 2,790,000* | 53,100,000* | 310,000* |

## Recommendation
Keep `ArrayList` for a playlist dominated by indexed jumps, iteration and appending. Stop recommending it when frequent front insertions/removals dominate. For the changed Up Next requirement, use `ArrayDeque`, because adding at both ends and removing from the front are its intended operations. A plain array with manual shifting has O(n) front/middle insertion and O(1) indexed access, but requires manual resizing.

---

# Q3 – hashCode–equals contract (6 marks)

## Contract
If two objects are equal according to `equals`, they must return the same `hashCode`. Unequal objects may share a hash code. HashSet first uses the hash to identify a bucket and then uses equality checks among candidates.

## Tests and predictions
1. Two different registration numbers plus a repeated scan of the first ID: expected unique count 2.
2. Same registration number with a different display name: expected unique count 1 for that ID because registration number defines identity.
3. Use a sorted set with an explicit comparator to demonstrate that ordering/equality policy must be consistent with the intended identity rule.

The corrected `Student` class uses immutable `regNo` in both `equals` and `hashCode`. This prevents the key identity from changing while the object is in a hash set. If a mutable field used in hashing changes after insertion, lookup/removal may fail because the object can remain in the bucket chosen by its old hash.

## Complete-the-code result
Expected output for the supplied code after implementing `hashCode`:
`Unique students: 2`

Before fixing the supplied version, record the observed output. Because the class overrides `equals` but not `hashCode`, equal objects can have different identity-based hashes; the exact output is not guaranteed and may vary by run/JVM.

## Alternative structures
A `TreeSet` with a comparator on registration number gives O(log n) add/lookup and sorted order. A `HashMap<String,Student>` keyed by registration number gives average O(1) put/get and makes identity explicit. Both require O(n) storage for n unique students.

---

# Q4 – Map selection for pharmacy inventory (6 marks)

## Comparison
`HashMap` offers average O(1) put/get and no ordering guarantee. `TreeMap` maintains key order with O(log n) put/get and O(n) ordered iteration. `LinkedHashMap` preserves insertion order with average O(1) put/get and O(n) iteration. The code-order report naturally fits TreeMap; insertion-order report naturally fits LinkedHashMap. A HashMap can be copied into a TreeMap for a code-order report, adding O(n log n) construction cost.

## Experiment
`PharmacyMaps.java` uses 1,000, 10,000 and 50,000 medicine entries and times 100,000 lookups. It also prints the first three keys for code-order and insertion-order reports. Repeat runs and preserve raw outputs.

| Map | put | get | Iteration | Natural report |
|---|---|---|---|---|
| HashMap | Average O(1) | Average O(1) | O(n) | Neither order guaranteed |
| TreeMap | O(log n) | O(log n) | O(n) | Code order |
| LinkedHashMap | Average O(1) | Average O(1) | O(n) | Insertion order |

## Illustrative lookup snapshot (simulated; nanoseconds for 100,000 lookups)
| Medicines | HashMap | TreeMap | LinkedHashMap |
|---:|---:|---:|---:|
| 1,000 | 24,500,000* | 68,400,000* | 27,100,000* |
| 10,000 | 25,300,000* | 103,500,000* | 27,800,000* |
| 50,000 | 26,900,000* | 132,000,000* | 29,100,000* |

## Deployment decision
If both report orders are mandatory and lookup volume is high, maintain a HashMap for lookup and use a TreeMap/LinkedHashMap view or a separately maintained ordered index according to update/report frequency. Define the lookup service-level target with the pharmacy before deployment; the assignment does not prescribe a numeric threshold. Hashtable is generally not preferred in new code because its legacy API synchronizes individual operations; modern concurrent requirements are better addressed explicitly with ConcurrentHashMap or external coordination.

## Alternative: sorted array
Binary search lookup is O(log n). Insertion into a sorted array is O(n) due to shifting. Code-order reporting is O(n) because the array is already sorted. Storage is O(n), with compact contiguous references but resizing/capacity overhead.

---

# Q5 – Generic leaderboard (7 marks)

## Design
A separate `Comparator<Team>` is appropriate because ranking policy may vary between competitions and the same generic leaderboard can rank other entity types. `Leaderboard<T>` stores values and accepts `Comparator<? super T>`, allowing a comparator for T or one of its supertypes. `List<? extends T>` safely accepts a list of subtype values when adding a batch.

## Ordering rule
Compare points descending, then goal difference descending, then team name ascending. The comparator chain expresses each tie-breaker explicitly and avoids subtraction-based comparison, which can overflow.

## Validation
Expected output:
`[Eagles(9,6), Bisons(9,4), Panthers(9,4), Cobras(6,1)]`

Bisons is above Panthers because points and goal difference are equal, so alphabetical order places Bisons first. The ranking method sorts a copy, preserving insertion order in the internal list.

## Complexity
Sorting n teams takes O(n log n) comparisons in the general case; the copied list requires O(n) additional space. A `Comparable<Team>` implementation is suitable when one natural order is universal. A Comparator is easier to reuse for players, colleges and alternative ranking rules. Three stable sorts also require O(n log n) per sort, approximately three sorting passes, and are less direct than one comparator chain.

---

# Q6 – Priority, order and fairness in triage (8 marks)

## Design
A FIFO queue alone cannot select the most severe patient. The priority queue orders by ascending severity number (1 is most critical), then ascending arrival sequence number. The monotonically increasing counter provides FIFO ordering among equal severities.

## Hand-checked example
Admission sequence:
Ravi S3, Meena S1, John S3, Asha S5, Kumar S1, Lata S2.

Expected treatment:
`Meena(S1) Kumar(S1) Lata(S2) Ravi(S3) John(S3) Asha(S5)`

Printing a PriorityQueue directly does not guarantee sorted treatment order; repeatedly polling it produces priority order.

## Complexity and growth
PriorityQueue add and poll are O(log n), peek is O(1), and storage is O(n). Test at least three queue sizes (for example 1,000, 10,000 and 50,000) and record repeated add/poll timings.

For scale illustration, log2(n) is approximately 10, 13 and 16 for n = 1,000, 10,000 and 50,000 respectively. This is a complexity illustration, not a measured duration.

A sorted LinkedList requires O(n) to find insertion position and O(1) removal at the head. Five FIFO queues (one per severity) give O(1) admission and O(1) selection when checking the five queue heads, with O(n) total storage.

## Starvation and fairness
A severity-5 patient can starve if more severe patients continuously arrive. A practical policy is aging: after a defined waiting threshold, promote the effective priority by one level, while never allowing a less urgent patient to overtake a critical emergency without a documented clinical rule. The policy must be approved by clinical governance and validated against patient safety requirements.

## Deployment decision
The data structure can handle 5,000 daily arrivals in asymptotic terms, but deployment suitability cannot be concluded from daily volume alone. Peak concurrent arrivals, latency targets, persistence, crash recovery, clinical workflow and fairness policy must be tested.

---

# Q7 – Diagnose, redesign and validate (13 marks)

## A. Diagnose
The Q1 repeated String concatenation workload is selected. Each loop iteration constructs a new immutable string and copies the accumulated prefix. As the ticket contains more fields, repeated copying grows disproportionately. This is a structural allocation/copying bottleneck.

## B. Objective
For the same field count and number of tickets, reduce total construction time and temporary string allocation while preserving byte-for-byte ticket content. Measure both approaches on the same laptop, JVM and workload.

## C. Revised design
Replace repeated `+` inside the loop with a method-local `StringBuilder`. Pre-size its capacity when a reasonable output-size estimate is available. Keep the builder local to one ticket so concurrent requests do not share mutable state.

Pseudocode:
1. Create a StringBuilder with estimated capacity.
2. For each ticket field, append the field value and delimiter.
3. Convert to String once after all fields are appended.
4. Return the immutable ticket string.

The redesign uses mutable buffer capacity and may allocate a final String. It trades temporary immutable-string creation for a resizable buffer. The builder is not thread-safe, which is acceptable when confined to one request/thread.

## D. Prediction
For large field counts, StringBuilder should reduce repeated copying and allocation. The improvement should be small or negligible for a very short ticket. The prediction must be checked with the same input sizes and repeated measurements.

## E. Implementation and validation
`StringRedesign.java` measures the original concatenation and revised builder approaches for 10, 50, 100 and 250 fields. Run it repeatedly. Verify output equality separately by comparing the two returned strings for every test size before using the timing result.

| Fields | Rounds | Original mean (ns) | Builder mean (ns) | Improvement |
|---:|---:|---:|---:|---|
| 10 | 500 | 1,850,000* | 610,000* | 67.0%* |
| 50 | 100 | 9,800,000* | 790,000* | 91.9%* |
| 100 | 50 | 18,100,000* | 810,000* | 95.5%* |
| 250 | 20 | 36,200,000* | 940,000* | 97.4%* |

Improvement percentage = ((original time − redesigned time) / original time) × 100.

## F. Final defence
The redesign is justified if measured large-workload results show lower time while output remains identical. It may be unnecessary for a tiny fixed string, where readability can outweigh a negligible timing difference. Builder capacity estimation and local confinement are important trade-offs. Insert the actual laptop measurements before final submission.

---

# Final submission checklist
- [ ] Run all seven Java programs on the student's laptop.
- [ ] Save raw benchmark outputs under `Experimental_Data`.
- [ ] Repeat timing experiments and calculate means.
- [ ] Add terminal screenshots for each question.
- [ ] Complete the measurement tables in Q1, Q2, Q4, Q6 and Q7.
- [ ] Record the pre-fix and post-fix Q3 outputs.
- [ ] Confirm all programs compile and expected functional outputs match.
- [ ] Add the actual GitHub repository URL to the final cover page.
- [ ] Commit each task separately and invite faculty if the repository is private.
