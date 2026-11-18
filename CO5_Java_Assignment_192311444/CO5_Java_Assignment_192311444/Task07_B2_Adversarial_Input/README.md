# B2 – Engineer an Adversarial Input (5 marks)
Run UnsafeLogin and SafeLogin against a disposable local test database with a controlled test user. Demonstrate a tautology/comment-style crafted input only in this local assignment database; do not use it against real accounts or systems. Unsafe SQL concatenation can change predicate structure; PreparedStatement binds the payload as a literal value and should reject it.

The file adversarial demo includes a wrong-field-count row, a non-numeric amount and an empty line. `PassStore.find()` skips these invalid records and continues scanning. This reveals why field-count and type validation are required; otherwise malformed records may crash parsing or be returned as valid.

Quote escaping and regex checks are weaker than parameter binding because SQL syntax, character encodings and database dialects create edge cases. PreparedStatement separates SQL structure from values. Validation still remains useful for domain constraints, but is not a substitute for binding.
