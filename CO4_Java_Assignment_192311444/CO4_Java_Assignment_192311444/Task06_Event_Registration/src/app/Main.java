package app;
import javax.swing.SwingUtilities;
import app.ui.RegistrationFrame;
public class Main {
    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new RegistrationFrame().setVisible(true)); }
}
