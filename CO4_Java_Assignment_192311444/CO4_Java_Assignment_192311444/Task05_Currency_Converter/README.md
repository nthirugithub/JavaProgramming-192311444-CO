# Task 5 – Four event-handling styles
ConverterDemo opens four equivalent windows: (a) frame implements ActionListener, (b) named inner class, (c) anonymous inner class, (d) lambda. Both Convert and Enter trigger conversion; invalid values show a label and dialog.

| Style | Readability | Access | Reuse | Change effort |
|---|---|---|---|---|
| Frame listener | Simple but mixes roles | Direct | Low | Easy initially; frame grows |
| Named inner | Explicit | Direct | Good | Change handler once |
| Anonymous | Local | Direct | Limited outside registration | Moderate |
| Lambda | Concise | Lexical | Good if stored | Low for short action |

For larger applications use a named handler or shared Swing Action. A method reference (`this::convert`) works when signatures match. A shared Action can coordinate button, menu item and keyboard shortcut state.
