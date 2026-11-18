# Task 3 – Layout decisions
Main JFrame uses BorderLayout. Header uses nested BorderLayout (title NORTH, search row SOUTH); search controls use FlowLayout; JTable inside JScrollPane occupies CENTER and grows; bottom controls use FlowLayout. Nested panels preserve preferred control sizes while the results region expands.

GridBagLayout gives detailed constraints and weights but requires more setup. Absolute positioning (`setLayout(null)`) requires manual bounds and does not adapt well to resize, fonts or display scaling. For c components, nested layouts and GridBagLayout use O(c) component/constraint storage and layout work per pass; absolute positioning stores O(c) bounds but moves resizing responsibility into application code.
