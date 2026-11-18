# Q3 – Resource safety and debugging (7)
Resources are output BufferedWriter and one BufferedReader per input. Manual close after the read loop leaks a reader if read/open fails before close; writer closes in finally. TWR closes in reverse declaration order even on exceptions. If body and close both fail, body exception is primary and close exception appears in getSuppressed().

For f files and N bytes, processing is O(N+f). TWR sequentially holds O(1) handles; manual leaks can accumulate O(f). TWR alone does not make writes atomic, validate data, prevent disk-full or guarantee durability. Debug at reader creation/read/close; instrument wrappers with open/close counters because OS handle counts are platform-dependent.
