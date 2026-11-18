# Q2 – User-defined exceptions and propagation (7)
Hierarchy: Exception→ExamException (checked)→SubmissionStorageException (checked). Storage catches IOException and wraps it with original cause; service propagates with throws; controller catches ExamException and displays a safe message. Without passing the cause, getCause() is null; dereferencing it throws NullPointerException and loses original type/stack.

Expected first lines: Saved answer01.pdf / Error: Could not store answer02.pdf / Cause: Disk quota exceeded. Test success, quota, missing directory/permission, exam closed, unregistered student and oversized answer. Checked exceptions require catch/throws; unchecked exceptions do not. Stack capture cost grows with stack depth; chaining retains cause objects and traces.

Joint: storage-specific IOException should be translated to meaningful SubmissionStorageException so UI is not coupled to filesystem implementation and can present safe, actionable feedback.
