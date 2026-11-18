# Q7 – Concurrent fault-tolerant processor (10)
Processes 10,000 independent score files. Each Callable-like submitted task reads, parses and validates 0..100; missing/corrupt/invalid files return a failure result rather than stopping the run. Future.get waits and surfaces unexpected task failures. ConcurrentLinkedQueue stores shared outcomes. Measures pool sizes 1,2,4,8.

Pseudocode: enumerate; create fixed pool p; submit one task/file; validate and compute; convert expected per-file failure to result; collect futures; aggregate successes/failures/scores; shutdown; report. Relationship: ResultProcessor→ExecutorService→Callable tasks→Future results→ConcurrentLinkedQueue.

Pool size may not scale linearly for disk-bound workloads, storage contention, short tasks or context-switch overhead. parallelStream uses common ForkJoinPool with less explicit control; one thread per file creates 10,000 stacks and scheduling pressure; fixed pool caps workers. Production improvement: bounded in-flight submission and explicit cancellation/shutdown policy.
