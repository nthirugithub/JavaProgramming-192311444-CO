# Task 2 – Hospital Outpatient Appointment Desk

## Event model
| Source | Event | Listener | Action |
|---|---|---|---|
| Department combo | ItemEvent | ItemListener | Refresh doctor list |
| Doctor combo | ItemEvent | ItemListener | Refresh time slots |
| Slot combo | ItemEvent | ItemListener | Store selected slot |
| Book button | ActionEvent | ActionListener | Validate and book |
| Patient ID field | ActionEvent | ActionListener | Validate/lookup |
| Window close | WindowEvent | WindowAdapter | Confirm if booking is in progress |

## Component hierarchy
```text
JFrame AppointmentDesk (BorderLayout)
├── JLabel heading (NORTH)
├── JPanel form (GridLayout 4x2, CENTER)
│   ├── JLabel + JTextField patient ID
│   ├── JLabel + JComboBox department
│   ├── JLabel + JComboBox doctor
│   └── JLabel + JComboBox slot
└── JPanel actions (FlowLayout, SOUTH)
    ├── JButton Book
    └── JLabel status
```

## Pseudocode – RefreshDoctorList
1. START
2. READ selectedDepartment
3. CLEAR doctorComboBox
4. SET doctors = doctorMap.get(selectedDepartment)
5. FOR each doctor in doctors: ADD doctor to doctorComboBox
6. IF doctors is empty: disable combo and show no-doctors message
7. ELSE enable combo and select first doctor
8. END

## Pseudocode – ValidateAndBook
1. START; READ patientId, department, doctor, slot
2. IF any required value is missing: display error and STOP
3. VALIDATE patient ID format; if invalid display error and STOP
4. SET bookingInProgress = true
5. ASK service to verify slot availability
6. IF unavailable display unavailable message; ELSE save and show confirmation
7. SET bookingInProgress = false
8. END

Event trace: construct JFrame → initialize components → register listeners → setVisible → user event → listener callback → service → update UI → windowClosing → confirmation if booking in progress → dispose. An applet instead has init/start/paint/stop/destroy lifecycle callbacks; JFrame does not use the applet lifecycle.

Alternative: Preload Map<String,List<Doctor>> once: O(d+m) storage and O(m) load for d departments and m doctors; each department change is O(1) average map lookup plus O(m_d) to populate UI. Querying DB on every change incurs repeated network/query latency and load, with less cached memory but slower interaction.
