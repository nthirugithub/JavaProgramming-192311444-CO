# CO3 Pair Assignment Report – 50 Marks
**Student A:** N Thirrumalaa – 192311444  
**Student B:** VEDA KRISHNAN V A – 192324293  
**JDK:** Java 25.0.1

This report follows the supplied CO3 PDF. Both students must individually execute every task in their own repository. Replace experiment prompts with actual run observations. No benchmark values or screenshots are fabricated.

## Individual contribution record
| Q | Student A primary | Student B primary | Joint |
|---|---|---|---|
| 1 | Throwable hierarchy, failure table, catch order/finally | Five test inputs, catch trace, stack trace, broad catch | ATM design/alternative |
| 2 | Exception hierarchy, propagation, checked/unchecked rationale | Scenarios, chained trace, cause-loss test table | Meaningful storage exception |
| 3 | Debugging, failing input, resource leak experiment/logging | Resource inventory, TWR close order, suppressed exception | Robustness decision |
| 4 | Thread creation/life cycle, start/run/join | Ten-run finish order, priority and daemon experiments | Scheduler explanation |
| 5 | Reproduce race, interleaving, critical section | Method/block timing, static synchronization, intrinsic lock | Deployment choice |
| 6 | Producer-consumer design, wait/while/notifyAll | Deadlock demonstration, Coffman conditions, lock ordering | Progress/alternative |
| 7 | Executor/Callable/Future design, collection, failures | Implementation, corrupt/missing injection, pool measurements | Defend pool size |

## Q1 – ATM (6)
Checked: custom invalid-account/insufficient-funds exceptions and IOException for card-reader failure. Unchecked: NumberFormatException and IllegalArgumentException. Error: OutOfMemoryError. Catch narrow types before broad; finally performs cleanup except abrupt termination. One catch(Exception) is only suitable at final boundary; loses recovery specificity and does not catch Error. Alternative Scanner pre-validation/status codes has O(1) fixed-input time/space; exception stack capture costs more than normal return.

Student B test matrix: valid, alphabetic, negative, insufficient balance, missing card file, zero. Record which catch and finally for each. Temporarily remove numeric catch to capture uncaught stack trace.

## Q2 – Exam portal (7)
Hierarchy Exception→ExamException→SubmissionStorageException. Storage wraps IOException with cause; service propagates; controller handles safe user message. Without cause constructor getCause is null and root trace lost. Expected: Saved answer01.pdf; Error: Could not store answer02.pdf; Cause: Disk quota exceeded. Test quota, permission, missing path, closed exam, unregistered student and oversize file. Stack capture proportional to depth; cause chain retains references/traces.

## Q3 – Resource safety (7)
Manual reader leaks if exception occurs before close; writer is closed in finally. TWR closes reverse declaration order; body exception remains primary and close exception is suppressed. For f files and N bytes time O(N+f); TWR sequential handle count O(1); manual leak can reach O(f). TWR does not guarantee atomic output or data validity. Use debugger and instrumented open/close counters.

## Q4 – Threads (7)
Thread subclass uses inheritance; Runnable separates task. start creates new thread; run direct call does not. sleep timed waiting; join waits; isAlive checks. States NEW, RUNNABLE, BLOCKED, WAITING, TIMED_WAITING, TERMINATED. Priority is a hint. Daemon does not keep JVM alive. Fixed pool p bounds stacks; ideal balanced workload about n/p batches plus overhead. Record finish order across ten runs.

## Q5 – Seat booking (6)
Race: two threads both read false then both mark and increment. Critical section is check/mark/increment. synchronized locks this monitor; block narrows critical region; static synchronized locks Class object. AtomicBoolean compareAndSet alternative uses O(s) space for s seats and O(1) expected operation. Record ten unsynchronized runs and 10k timing for method/block versions.

## Q6 – Deadlock and communication (7)
Buffer invariant 0≤count≤5; while protects against spurious wakeups/changed conditions. wait requires monitor and releases it; sleep does not. notifyAll wakes waiters to recheck. Expected sum 210. Deadlock conditions: mutual exclusion, hold-and-wait, no preemption, circular wait. Consistent lock ordering removes circular wait. ArrayBlockingQueue(5) alternative encapsulates synchronization, O(1) typical put/take and O(5) space.

## Q7 – Concurrent processor (10)
10,000 independent score files; per-file missing/corrupt failures become result records. ExecutorService submits tasks, Callable produces result, Future retrieves, ConcurrentLinkedQueue aggregates. Compare pools 1,2,4,8 with actual times. More threads may hurt disk-bound work due to contention/context switches. One thread/file has 10,000 stacks; fixed pool caps worker stacks. parallelStream uses common pool and offers less explicit control.

| Pool | Files | Success | Failed | Time ns |
|---:|---:|---:|---:|---:|
| 1 | 10,000 | actual | actual | actual |
| 2 | 10,000 | actual | actual | actual |
| 4 | 10,000 | actual | actual | actual |
| 8 | 10,000 | actual | actual | actual |

## Final checklist
- Both students execute all seven tasks individually.
- Record Q1/Q2 test paths and full chained stack trace.
- Q3 debug and count resource opens/closes.
- Q4 ten-run ordering, priority and daemon experiment.
- Q5 unsynchronized ten-run and 10k timings.
- Q6 capacity observations and controlled deadlock/repair.
- Q7 run 10k files at pool sizes 1,2,4,8 and retain raw data.
- Create separate required GitHub repositories and commit progressively at least once per task.
