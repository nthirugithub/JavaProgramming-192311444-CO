# Task 1
Deficiencies: (1) UI and business/database logic are coupled. (2) `==` compares String identity, not content. (3) invalid/empty numeric input can throw NumberFormatException. (4) SQL concatenation risks SQL injection. (5) JDBC and sleep on the event-dispatch thread freeze the window. (6) Receipt is not implemented and else incorrectly clears for every other action. (7) no connection cleanup, feedback, checked-exception handling or window-close handler.

Repair: FeeWindow collects/displays; FeeService validates and prepares a receipt preview. A real payment DAO should use PreparedStatement and try-with-resources; long-running work should run in SwingWorker.

Catch specific exceptions before broader ones. A broad catch can be used at the final UI boundary for logging and safe feedback, but it hides distinctions and may mask programming defects.

Alternative: one listener per button avoids an if/else chain of up to k command comparisons. Branch dispatch is O(k) worst case; direct listener selection is approximately O(1). k separate listeners consume O(k) space; one shared listener uses O(1) listener objects.
