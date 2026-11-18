# Q1 – ATM exception handling (6)
Checked domain exceptions: InvalidAccountException, InsufficientFundsException, CardReaderException extends IOException. Unchecked: NumberFormatException and IllegalArgumentException. Error examples such as OutOfMemoryError are not ordinary recoverable input failures. Catch specific types before broad types; finally performs cleanup after normal/exceptional flow except abrupt process/JVM termination.

Test at least: valid 123456/500; letters abc/500; negative 123456/-20; insufficient 123456/9000; missing card file 123456/500/false; zero amount. Record catch path and finally execution. To show stack trace, temporarily remove NumberFormatException catch and enter letters. Putting catch(Exception) before narrower catches makes narrower catches unreachable.

Hierarchy: Throwable→Error; Throwable→Exception→IOException→CardReaderException; Exception→RuntimeException→IllegalArgumentException/NumberFormatException. A single catch(Exception) is suitable only at a final logging/UI boundary; it loses specific recovery and does not catch Error.

Alternative Scanner.hasNextLong/hasNextDouble and status codes: O(1) time/space for fixed input; explicit checks improve normal control flow. Exception creation captures stack trace and is costlier than returning an error code.
