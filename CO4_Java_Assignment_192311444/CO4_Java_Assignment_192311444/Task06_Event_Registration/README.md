# Task 6 – Campus Event Registration
Architecture: `ui/RegistrationFrame` (Swing UI), `service/RegistrationService` (validation/rules), `dao/*` (JDBC), `model/*` (domain records), `app/Main` (entry point).

Features: registration form; event choices loaded from MySQL; JTable with event filter and 100-row LIMIT/OFFSET pagination; CSV export of current page; duplicate prevention via UNIQUE(student_reg_no,event_id); user-friendly validation and database messages; SwingWorker moves JDBC calls off the Event Dispatch Thread; try-with-resources closes JDBC resources.

Run `database/schema.sql` on MySQL 8.x. Set `DB_URL`, `DB_USER`, `DB_PASSWORD`. Add MySQL Connector/J 9.x JAR to `lib`. The application is a coursework prototype; authentication, capacity enforcement under concurrent load, audit logging and keyset pagination are production refinements. Do not commit DB passwords.
