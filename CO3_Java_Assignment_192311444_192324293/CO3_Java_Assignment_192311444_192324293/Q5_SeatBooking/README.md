# Q5 – Cinema seat race (6)
Unsynchronized check-then-act can interleave: T1 and T2 both read false, both set true and increment. Critical section is check, mark, increment. Synchronized method uses this object's intrinsic monitor. A synchronized block can restrict lock scope. static synchronized locks the Class object for shared static state.

Student A: reproduce unsynchronized race over ten runs and record duplicate count. Student B: time 10,000 attempts for method/block versions. Alternative AtomicBoolean[] uses O(s) storage for s seats and compareAndSet O(1) expected attempt; ReentrantLock supports tryLock/fairness. For 20 screens use per-screen/seat lock to reduce contention and database uniqueness/transaction for multi-process correctness.
