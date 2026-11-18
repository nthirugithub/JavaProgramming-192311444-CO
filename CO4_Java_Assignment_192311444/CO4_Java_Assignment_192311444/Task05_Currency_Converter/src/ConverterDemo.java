import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ConverterDemo {
    private static double rate(String c) { return "USD".equals(c) ? 0.012 : 0.011; }
    private static class Base extends JFrame {
        final JTextField amount = new JTextField(9);
        final JComboBox<String> target = new JComboBox<>(new String[]{"USD","EUR"});
        final JLabel result = new JLabel(" ");
        final JButton button = new JButton("Convert");
        Base(String title) {
            super(title); setLayout(new FlowLayout());
            add(new JLabel("INR")); add(amount); add(target); add(button); add(result);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE); pack(); setLocationByPlatform(true);
        }
        void convert() {
            try {
                double value = Double.parseDouble(amount.getText().trim());
                if (!Double.isFinite(value) || value < 0) throw new NumberFormatException();
                String c=(String)target.getSelectedItem();
                result.setText(String.format("%.2f %s", value*rate(c), c));
            } catch (NumberFormatException ex) {
                result.setText("Invalid amount");
                JOptionPane.showMessageDialog(this,"Enter a valid non-negative amount.","Input error",JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    // (a) Frame itself implements ActionListener
    private static class FrameStyle extends Base implements ActionListener {
        FrameStyle() { super("(a) Frame listener"); button.addActionListener(this); amount.addActionListener(this); }
        @Override public void actionPerformed(ActionEvent e) { convert(); }
    }
    // (b) Named inner class
    private static class NamedStyle extends Base {
        NamedStyle() { super("(b) Named inner class"); Handler h=new Handler(); button.addActionListener(h); amount.addActionListener(h); }
        private class Handler implements ActionListener {
            @Override public void actionPerformed(ActionEvent e) { convert(); }
        }
    }
    // (c) Anonymous inner class
    private static class AnonymousStyle extends Base {
        AnonymousStyle() {
            super("(c) Anonymous inner class");
            ActionListener h=new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) { convert(); }
            };
            button.addActionListener(h); amount.addActionListener(h);
        }
    }
    // (d) Lambda version, one listener shared by both sources
    private static class LambdaStyle extends Base {
        LambdaStyle() {
            super("(d) Lambda");
            ActionListener h=e -> convert();
            button.addActionListener(h); amount.addActionListener(h);
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame[] fs={new FrameStyle(),new NamedStyle(),new AnonymousStyle(),new LambdaStyle()};
            int x=20;
            for(JFrame f:fs){f.setLocation(x,80);f.setVisible(true);x+=330;}
        });
    }
}
