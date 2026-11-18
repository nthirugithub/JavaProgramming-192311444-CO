# B4 – Final Engineering Decision (5 marks)
Migration reads a CSV-style pass file, validates field count, route and amount, records rejected line numbers/reasons, then loads valid records using the transaction/batch loader. It reports database product/version, JDBC driver/version and table names using DatabaseMetaData.

Production decision for 12 campuses and 100× growth: use a shared relational database with transactions, uniqueness/constraints, indexed pass ID/route columns, prepared statements, batch import and appropriate isolation. Text files are useful for interchange/archives but weak for concurrent updates and cross-campus search. Java serialization is not a shared multi-writer database and couples data to class versions.

Twelve separate serialized files require searching each file (O(total records) for a full scan), duplicated storage and explicit coordination/locking/merge logic for concurrent counters. A shared database supports centralized constraints, transactions, indexes and concurrent clients. It requires availability, backup, security and capacity planning.
