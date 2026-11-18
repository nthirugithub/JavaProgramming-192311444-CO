# A1 – Design a File Format (7 marks)

## Record layout (text)
Use UTF-8 CSV-style rows in this field order:
`passId,regNo,name,routeNo,boardingStop,validityDate,amountPaid`
Use a CSV quoting convention: fields containing comma, quote or newline are enclosed in double quotes; each embedded quote is doubled. A simple `String.split(",")` is not a valid general CSV parser. For this coursework implementation, the B1 five-field TODO format is retained exactly as supplied by the PDF.

## Record layout (binary DataOutputStream)
Write fields in the same logical order using `writeUTF(passId)`, `writeUTF(regNo)`, `writeUTF(name)`, `writeInt(routeNo)`, `writeUTF(boardingStop)`, `writeUTF(validityDate)` (ISO-8601), `writeDouble(amountPaid)`. The corresponding reader must use exactly the same order and types. `writeUTF` uses modified UTF-8 with a length prefix; it is not a fixed-width field.

## Operations and stream choice
- Add: `BufferedWriter` over `Files.newBufferedWriter` in CREATE + APPEND mode; buffering reduces per-character I/O overhead.
- Find by ID: `BufferedReader` over a character reader; scan line by line, parse/validate each record, compare ID.
- List by route: same buffered scan; parse each valid record and emit records whose route matches.
- Binary alternative: `DataOutputStream` over `BufferedOutputStream` and `FileOutputStream`; read with `DataInputStream` over `BufferedInputStream`.

## SIMATS-style pseudocode – AddPass
1. START
2. VALIDATE all required pass fields
3. OPEN pass file using BufferedWriter in append mode
4. ENCODE fields using the defined delimiter/escaping convention
5. WRITE encoded record and newline
6. FLUSH and CLOSE writer
7. IF I/O error THEN report failure and preserve error details
8. END

## SIMATS-style pseudocode – FindPass
1. START
2. IF file does not exist THEN return NOT_FOUND with file-missing status
3. OPEN BufferedReader
4. WHILE next line exists
   4.1 PARSE line using the defined CSV parser
   4.2 IF malformed THEN record damaged-line diagnostic and CONTINUE
   4.3 IF pass ID matches THEN CLOSE reader and RETURN record
5. END WHILE
6. CLOSE reader and RETURN NOT_FOUND
7. END

## SIMATS-style pseudocode – ListByRoute
1. START
2. CREATE empty result list
3. OPEN BufferedReader
4. FOR each line
   4.1 PARSE and validate line
   4.2 IF valid and route equals requested route THEN append to result
   4.3 ELSE record damaged-line diagnostic
5. END FOR
6. CLOSE reader and DISPLAY result list
7. END

## Damaged data and failure recovery
- Damaged line: validate field count, numeric route/amount and date; skip invalid line while logging line number and reason.
- Missing file: distinguish absent file from empty file; return a clear status. Create only for add operation.
- Interrupted append: a partial final line may remain. On next read, detect missing fields/invalid terminator and quarantine/report it. For stronger guarantees, write a complete temporary file and atomically rename it, or use a journal/checksum; flush does not guarantee physical media durability.
- RandomAccessFile alternative: if records have fixed byte length R, record i starts at `i×R`; direct seek is O(1) only when record index is already known. Searching by pass ID without an index still scans O(n). Fixed-width padding wastes space for short names/stops; a separate ID-to-offset index is needed for O(1) average lookup.
