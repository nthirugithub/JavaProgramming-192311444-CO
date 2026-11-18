# Programming in Java – Assignment 4 (CO4): Event-Driven GUI Design
**Student:** N Thirrumalaa | **Register:** 192311444 | **SIMATS** | **BE CSE – Core** | **4th Year** | **JDK 25.0.1**

> This report is a draft grounded in the supplied CO4 assignment. Add actual execution screenshots and observed results before submission. Do not claim tests were executed unless you ran them.

## Task 1 – GUI Design Review (6)
**Deficiencies:** UI/business/database logic are coupled; `==` compares String references; invalid numeric input can throw; SQL concatenation risks injection; JDBC and sleep block the event-dispatch thread; Receipt is not implemented; `else` incorrectly treats every other action as Clear; no useful feedback; no resource cleanup; checked exceptions and window closing are not handled.

**Repair:** `FeeWindow` gathers input and displays feedback; `FeeService` validates and prepares a receipt preview. A real payment DAO should use PreparedStatement and try-with-resources, with long operations in SwingWorker.

**Defence (under 100 words):** The redesign separates presentation from business rules. FeeService can be tested without constructing a window, while FeeWindow can be tested for event wiring and feedback. Validation changes do not require GUI changes, and GUI changes do not alter payment rules. This reduces coupling, improves reuse and isolates defects.

**Alternative:** one listener with k command branches has O(k) worst-case comparisons; dedicated listener/action routing is approximately O(1) after source dispatch. k listener objects use O(k) space; one shared listener uses O(1) listener objects.

## Task 2 – Hospital Appointment Desk (8)
**Event model:** department combo/ItemEvent/ItemListener/refresh doctors; doctor combo/ItemEvent/ItemListener/refresh slots; slot combo/ItemEvent/ItemListener/update selection; Book button/ActionEvent/ActionListener/validate and book; patient ID field/ActionEvent/ActionListener/validate; window close/WindowEvent/WindowAdapter/confirm during booking.

**Hierarchy:** JFrame(BorderLayout) → heading JLabel NORTH; form JPanel(GridLayout 4×2) CENTER with patient ID, department, doctor and slot labels/controls; actions JPanel(FlowLayout) SOUTH with Book and status.

**Pseudocode – RefreshDoctorList:** START; read department; clear doctor combo; retrieve `doctorMap.get(department)`; add doctors; if empty disable and display message, else enable and select first; END.

**Pseudocode – ValidateAndBook:** START; read fields; reject missing/invalid values; set booking-in-progress; check availability; save if available or display error; clear booking-in-progress; END.

**Trace:** construct → initialize components → register listeners → show window → event → listener → service → UI update → windowClosing → confirmation if needed → dispose. Applet lifecycle is init/start/paint/stop/destroy; JFrame does not use it.

**Alternative:** caching Map<String,List<Doctor>> costs O(d+m) storage and O(m) initial load; department change is O(1) average lookup plus O(m_d) to populate UI. Query-per-change incurs repeated database/network latency and load, trading cache memory for slower interaction.

## Task 3 – Library Search Screen (9)
Main frame BorderLayout; header nested BorderLayout (title NORTH, search row SOUTH); search row FlowLayout; JScrollPane/JTable CENTER grows; footer controls FlowLayout and status SOUTH.

**Resize evidence plan:** capture 620×360, 820×520 and 1100×700. The results region should expand while search controls and bottom buttons retain preferred sizes.

**Alternative:** GridBagLayout gives detailed constraints/weights but more setup. Absolute positioning with setLayout(null) is fragile under resizing, font scaling and platform differences. For c components, layouts store O(c) components/constraints and perform O(c) work per layout pass; absolute layout stores O(c) bounds but requires manual resizing logic.

## Task 4 – Event-Handling Forensics (7)
The original method is misspelled `mouseclicked`; correct callback is `mouseClicked`. Thus it is not overridden. `getGraphics()` draws transiently; minimize/restore triggers repaint and erases marks. The completed code stores click Points, updates count, calls repaint, and redraws in paint(). `@Override` makes the compiler detect the typo.

**Alternative BufferedImage:** point list uses O(n) storage and O(n) drawing per repaint. ARGB image uses O(width×height) pixels (about 4 bytes/pixel) and repaint copies/blits the raster. Point list is compact for sparse marks; image can be efficient for dense drawings.

## Task 5 – Currency Converter (7)
`ConverterDemo` includes frame listener, named inner class, anonymous inner class and lambda. Both Convert and Enter trigger conversion; invalid input displays a label and dialog.

| Style | Readability | Access | Reuse | Change |
|---|---|---|---|---|
| Frame listener | Simple, but mixes roles | Direct | Low | Easy initially |
| Named inner | Explicit | Direct | Good | Change one handler |
| Anonymous | Local | Direct | Limited outside registration | Moderate |
| Lambda | Concise | Lexical | Good if stored | Low for short actions |

For larger applications, named handlers or shared Swing Action are easier to test/reuse. Method reference `this::convert` is valid when signatures match; shared Action can coordinate buttons, menu items and shortcuts.

## Task 6 – Campus Event Registration (13)
**Users/screens/data/rules:** student/reception operator and committee staff; registration form, table/filter, export and dialogs; student registration number/name/email, event and timestamp; validate required fields, prohibit duplicate student-event registrations, keep DB work off EDT.

**Class architecture:** RegistrationFrame (UI) → RegistrationService (validation/business rules) → EventDao/RegistrationDao (JDBC) → MySQL. Model classes represent Event and Registration.

**Register pseudocode:** read fields; validate ID/name/email/event; if invalid display message; otherwise launch SwingWorker; background service inserts with PreparedStatement; database UNIQUE(student_reg_no,event_id) blocks duplicates; done() displays success/error and refreshes table.

**Filter pseudocode:** read filter and offset; SwingWorker calls DAO; DAO runs parameterized query with LIMIT/OFFSET; done() replaces table rows and updates status.

**JDBC safety:** PreparedStatement parameterizes user values. try-with-resources closes connections/statements/result sets. SwingWorker keeps database calls off the EDT; UI updates occur in done().

**Pagination:** loading all N=100,000 rows transfers and retains O(N) row data and can cause long startup/UI update work. Page size P uses O(P) client rows and reduces initial transfer. OFFSET may become expensive at large offsets; keyset pagination is a refinement.

**Test table:** valid registration → success; empty ID → validation; invalid email → validation; duplicate same event → duplicate warning and one row; same student/different event → allowed; filter → matching rows; export → CSV; stopped MySQL → error while UI remains responsive.

**Limitation:** coursework prototype does not implement authentication, capacity enforcement under concurrent load, full audit logging or keyset pagination. These are production refinements.

## Final checklist
- [ ] Compile and run each Java program on local JDK.
- [ ] Capture real Task 1, 3, 4 and 5 screenshots.
- [ ] Configure MySQL/Connector-J and test Task 6.
- [ ] Add screenshots and captions linked to requirements.
- [ ] Add GitHub URL to report and README.
- [ ] Make progressive commits, at least one per task.
