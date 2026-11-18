# Q4 – Thread creation and life cycle (7)
Extending Thread is direct but uses Java's single inheritance; Runnable separates task from execution and works with pools. start() creates/schedules a new thread; run() called directly is an ordinary method call. sleep() causes timed waiting; join() waits; isAlive() tests liveness. States: NEW→RUNNABLE (includes executing); TIMED_WAITING for sleep; WAITING for join; BLOCKED for monitor acquisition; TERMINATED after run returns.

Run ten times and record completion order. Priority is a scheduler hint, not a guarantee. Daemon threads do not keep JVM alive. Alternative fixed ExecutorService pool p bounds worker stacks; ideal balanced work is about n/p task batches plus overhead. One thread per station uses n stacks; fixed pool uses p.
