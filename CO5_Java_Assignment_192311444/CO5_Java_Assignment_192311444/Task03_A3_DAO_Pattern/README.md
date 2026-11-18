# A3 – DAO Pattern (5 marks)
Repeated sequence in StudentDB/CourseDB/MarksDB: obtain connection, create SQL, bind parameters, execute, iterate ResultSet, map rows to objects, close JDBC resources. Varying parts: table/column names, entity type, primary-key type, SQL and row mapper.

`Dao<T,K>` centralizes CRUD method shape; `StudentDao` implements SQL and mapping for Student. Object mapping: `Student.id` ↔ `student.student_id` (primary key); `registerNumber` ↔ `register_no`; `name` ↔ `student_name`; `email` ↔ `email`.

If SQL is placed in GUI/service classes, a renamed column may require edits in every consumer. DAO confines schema-specific SQL to the data-access layer. This is an architectural/maintenance benefit, not an inherent runtime speedup: both execute comparable SQL and have the same database-side complexity; DAO adds a small method-call/abstraction overhead and does not change O(query cost) data access.
