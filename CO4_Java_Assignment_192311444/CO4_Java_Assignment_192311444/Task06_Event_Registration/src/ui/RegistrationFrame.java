package app.ui;
import app.model.*;
import app.service.RegistrationService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.sql.SQLException;
import java.util.List;

public class RegistrationFrame extends JFrame {
    private final RegistrationService service=new RegistrationService();
    private final JTextField reg=new JTextField(11), name=new JTextField(14), email=new JTextField(16);
    private final JComboBox<app.model.Event> eventBox=new JComboBox<>();
    private final JComboBox<String> filter=new JComboBox<>(new String[]{"ALL"});
    private final DefaultTableModel model=new DefaultTableModel(
        new Object[]{"ID","Register No.","Student","Event","Email","Registered At"},0) {
        @Override public boolean isCellEditable(int r,int c){return false;}
    };
    private final JTable table=new JTable(model);
    private final JLabel status=new JLabel("Ready");
    private final JButton submit=new JButton("Register");
    private int offset=0;
    private static final int PAGE=100;

    public RegistrationFrame() {
        super("Campus Event Registration – SIMATS");
        setLayout(new BorderLayout(8,8)); setJMenuBar(menuBar());
        add(form(),BorderLayout.NORTH); add(new JScrollPane(table),BorderLayout.CENTER); add(footer(),BorderLayout.SOUTH);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosing(java.awt.event.WindowEvent e) {
                if(JOptionPane.showConfirmDialog(RegistrationFrame.this,"Close application?","Confirm",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) dispose();
            }
        });
        setMinimumSize(new Dimension(850,500)); setSize(1050,650); setLocationRelativeTo(null);
        loadEvents(); loadRows();
    }
    private JMenuBar menuBar() {
        JMenuBar bar=new JMenuBar(); JMenu file=new JMenu("File"), help=new JMenu("Help");
        JMenuItem export=new JMenuItem("Export current page"), exit=new JMenuItem("Exit"), about=new JMenuItem("About");
        export.addActionListener(e->exportCsv()); exit.addActionListener(e->dispatchEvent(
            new java.awt.event.WindowEvent(this,java.awt.event.WindowEvent.WINDOW_CLOSING)));
        about.addActionListener(e->JOptionPane.showMessageDialog(this,"Campus Event Registration\nSwing + MySQL JDBC"));
        file.add(export); file.addSeparator(); file.add(exit); help.add(about); bar.add(file); bar.add(help); return bar;
    }
    private JPanel form() {
        JPanel p=new JPanel(new FlowLayout(FlowLayout.LEFT)); p.setBorder(BorderFactory.createTitledBorder("New Registration"));
        p.add(new JLabel("Register No.")); p.add(reg); p.add(new JLabel("Name")); p.add(name);
        p.add(new JLabel("Email")); p.add(email); p.add(new JLabel("Event")); p.add(eventBox);
        submit.addActionListener(e->registerAsync()); p.add(submit); return p;
    }
    private JPanel footer() {
        JPanel p=new JPanel(new BorderLayout()); JPanel controls=new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refresh=new JButton("Refresh"), prev=new JButton("Previous"), next=new JButton("Next"), export=new JButton("Export CSV");
        controls.add(new JLabel("Filter:")); controls.add(filter); controls.add(refresh); controls.add(prev); controls.add(next); controls.add(export);
        refresh.addActionListener(e->{offset=0;loadRows();}); filter.addActionListener(e->{offset=0;loadRows();});
        prev.addActionListener(e->{offset=Math.max(0,offset-PAGE);loadRows();});
        next.addActionListener(e->{offset+=PAGE;loadRows();}); export.addActionListener(e->exportCsv());
        p.add(controls,BorderLayout.CENTER); p.add(status,BorderLayout.SOUTH); return p;
    }
    private void loadEvents() {
        busy(true,"Loading events...");
        new SwingWorker<List<app.model.Event>,Void>() {
            protected List<app.model.Event> doInBackground() throws Exception {return service.events();}
            protected void done(){try{for(app.model.Event e:get()){eventBox.addItem(e);filter.addItem(e.name());}}
                catch(Exception ex){error("Could not load events",ex);}finally{busy(false,null);}}
        }.execute();
    }
    private void registerAsync() {
        String r=reg.getText(), n=name.getText(), em=email.getText(); app.model.Event ev=(app.model.Event)eventBox.getSelectedItem();
        busy(true,"Saving registration...");
        new SwingWorker<Void,Void>() {
            protected Void doInBackground() throws Exception {service.register(r,n,ev,em);return null;}
            protected void done(){try{get();status.setText("Registration saved.");JOptionPane.showMessageDialog(RegistrationFrame.this,"Registration completed.");
                reg.setText("");name.setText("");email.setText("");offset=0;loadRows();}
                catch(Exception ex){error("Registration failed",ex);}finally{busy(false,null);}}
        }.execute();
    }
    private void loadRows() {
        String selected=(String)filter.getSelectedItem(); if(selected==null)selected="ALL"; final String f=selected;
        busy(true,"Loading registrations...");
        new SwingWorker<List<Registration>,Void>() {
            protected List<Registration> doInBackground() throws Exception {return service.page(f,PAGE,offset);}
            protected void done(){try{model.setRowCount(0);for(Registration r:get())model.addRow(new Object[]{
                r.id(),r.studentRegNo(),r.studentName(),r.eventName(),r.email(),r.registeredAt()});
                status.setText("Page offset: "+offset);}
                catch(Exception ex){error("Could not load registrations",ex);}finally{busy(false,null);}}
        }.execute();
    }
    private void exportCsv() {
        if(model.getRowCount()==0){JOptionPane.showMessageDialog(this,"No rows on this page.");return;}
        JFileChooser chooser=new JFileChooser();chooser.setSelectedFile(new java.io.File("registrations.csv"));
        if(chooser.showSaveDialog(this)!=JFileChooser.APPROVE_OPTION)return;
        try(BufferedWriter w=Files.newBufferedWriter(chooser.getSelectedFile().toPath())) {
            for(int c=0;c<model.getColumnCount();c++){if(c>0)w.write(",");w.write(csv(model.getColumnName(c)));}w.newLine();
            for(int r=0;r<model.getRowCount();r++){for(int c=0;c<model.getColumnCount();c++){
                if(c>0)w.write(",");w.write(csv(String.valueOf(model.getValueAt(r,c))));}w.newLine();}
            status.setText("CSV exported.");
        }catch(IOException ex){error("CSV export failed",ex);}
    }
    private String csv(String s){return "\""+s.replace("\"","\"\"")+"\"";}
    private void busy(boolean b,String msg){submit.setEnabled(!b);if(msg!=null)status.setText(msg);}
    private void error(String title,Exception ex){
        Throwable t=ex.getCause()==null?ex:ex.getCause();String m=t.getMessage()==null?t.toString():t.getMessage();
        status.setText(title+": "+m);JOptionPane.showMessageDialog(this,m,title,JOptionPane.ERROR_MESSAGE);
    }
}
