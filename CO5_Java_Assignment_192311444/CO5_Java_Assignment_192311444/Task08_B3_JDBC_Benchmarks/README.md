# B3 – Statement vs PreparedStatement vs Batch (8 marks)
Run schema.sql, configure DB environment variables and Connector/J, then compile/run using the root README. The benchmark tests 10, 1,000, 10,000 and 100,000 rows (10-row correctness plus the required sizes). It uses one transaction per strategy to isolate insert cost and clears the test table between strategies. Record multiple runs and medians. Results depend on DB server, network, JDBC driver, indexes, autocommit and JVM warm-up.

The `PassLoader.loadAll()` implementation completes all three TODOs: binds five parameters, adds each row to batch, executes every 1,000, flushes remainder, commits and returns row count; on SQL/runtime failure it rolls back and rethrows; restores original autoCommit.

For auto-commit vs transaction and forced mid-list failure, add a test with a deliberately invalid numeric amount around halfway and compare row count after failure. The transactional loader must leave zero inserted rows. Auto-commit can retain earlier rows. Do not run destructive tests on a non-test database.
