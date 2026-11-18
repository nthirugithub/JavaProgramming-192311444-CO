import java.awt.*;
import java.awt.event.*;

public class FeeWindow extends Frame {
    private final TextField regNo = new TextField(18), amount = new TextField(18);
    private final Label status = new Label("Enter details and choose an action.");
    private final FeeService service = new FeeService();

    public FeeWindow() {
        super("Student Fee Payment – Design Demonstration");
        setLayout(new BorderLayout(8, 8));
        Panel form = new Panel(new GridLayout(2, 2, 8, 8));
        form.add(new Label("Registration number:")); form.add(regNo);
        form.add(new Label("Amount (INR):")); form.add(amount);
        Panel actions = new Panel(new FlowLayout());
        Button pay = new Button("Pay"), clear = new Button("Clear"), receipt = new Button("Receipt");
        actions.add(pay); actions.add(clear); actions.add(receipt);
        add(form, BorderLayout.CENTER); add(actions, BorderLayout.NORTH); add(status, BorderLayout.SOUTH);
        pay.addActionListener(e -> process());
        clear.addActionListener(e -> { regNo.setText(""); amount.setText(""); status.setText("Form cleared."); });
        receipt.addActionListener(e -> showReceipt());
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { dispose(); }
        });
        setSize(470, 190); setLocationRelativeTo(null);
    }
    private String validateInput() {
        return service.validateAndPrepareReceipt(regNo.getText(), amount.getText());
    }
    private void process() {
        try { status.setText(validateInput()); }
        catch (IllegalArgumentException ex) { status.setText("Error: " + ex.getMessage()); }
    }
    private void showReceipt() {
        try {
            Dialog d = new Dialog(this, "Receipt preview", true);
            d.setLayout(new BorderLayout());
            d.add(new Label(validateInput()), BorderLayout.CENTER);
            Button close = new Button("Close"); close.addActionListener(e -> d.dispose());
            d.add(close, BorderLayout.SOUTH); d.setSize(400, 110); d.setLocationRelativeTo(this); d.setVisible(true);
        } catch (IllegalArgumentException ex) { status.setText("Error: " + ex.getMessage()); }
    }
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new FeeWindow().setVisible(true));
    }
}
