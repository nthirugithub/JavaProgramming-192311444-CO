import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class LibrarySearchApp extends JFrame {
    private final JTextField query = new JTextField(20);
    private final JComboBox<String> category = new JComboBox<>(new String[]{"All","Java","Database","Networking"});
    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Book ID","Title","Category","Availability"}, 0);
    private final JTable table = new JTable(model);
    private final JLabel status = new JLabel("Ready");

    public LibrarySearchApp() {
        super("SIMATS Campus Library – Search");
        setLayout(new BorderLayout(8,8));
        JLabel title = new JLabel("Campus Library Catalogue", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        JPanel search = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton find = new JButton("Search");
        search.add(new JLabel("Search:")); search.add(query); search.add(new JLabel("Category:"));
        search.add(category); search.add(find);
        JPanel header = new JPanel(new BorderLayout()); header.add(title, BorderLayout.NORTH);
        header.add(search, BorderLayout.SOUTH); add(header, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton prev = new JButton("Prev"), next = new JButton("Next"), reserve = new JButton("Reserve");
        buttons.add(prev); buttons.add(next); buttons.add(reserve);
        JPanel footer = new JPanel(new BorderLayout()); footer.add(buttons, BorderLayout.CENTER);
        footer.add(status, BorderLayout.SOUTH); add(footer, BorderLayout.SOUTH);
        model.addRow(new Object[]{"B101","Effective Java","Java","Available"});
        model.addRow(new Object[]{"B102","Java: The Complete Reference","Java","Issued"});
        model.addRow(new Object[]{"B201","Database System Concepts","Database","Available"});
        model.addRow(new Object[]{"B301","Computer Networks","Networking","Available"});
        find.addActionListener(e -> filter()); query.addActionListener(e -> filter());
        prev.addActionListener(e -> status.setText("Previous page requested."));
        next.addActionListener(e -> status.setText("Next page requested."));
        reserve.addActionListener(e -> status.setText(table.getSelectedRow() < 0
                ? "Select a book first." : "Reservation request selected."));
        setDefaultCloseOperation(EXIT_ON_CLOSE); setMinimumSize(new Dimension(620,360));
        setSize(820,520); setLocationRelativeTo(null);
    }
    private void filter() {
        String q = query.getText().trim().toLowerCase();
        String c = (String) category.getSelectedItem();
        for (int i=model.getRowCount()-1; i>=0; i--) {
            String t=model.getValueAt(i,1).toString().toLowerCase();
            String cat=model.getValueAt(i,2).toString();
            if (!t.contains(q) || (!"All".equals(c) && !cat.equals(c))) model.removeRow(i);
        }
        status.setText("Matching sample rows displayed. Restart to restore the sample catalogue.");
    }
    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new LibrarySearchApp().setVisible(true)); }
}
