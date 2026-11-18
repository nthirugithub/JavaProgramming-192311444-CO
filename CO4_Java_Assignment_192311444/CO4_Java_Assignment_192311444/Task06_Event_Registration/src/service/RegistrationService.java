package app.service;
import app.dao.*;
import app.model.*;
import java.sql.*;
import java.util.*;
import java.util.regex.Pattern;
public class RegistrationService {
    private static final Pattern REG=Pattern.compile("[A-Za-z0-9-]{4,20}");
    private static final Pattern EMAIL=Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private final EventDao events=new EventDao();
    private final RegistrationDao registrations=new RegistrationDao();
    public List<Event> events() throws SQLException { return events.findAll(); }
    public void register(String reg,String name,Event event,String email) throws SQLException {
        String r=reg==null?"":reg.trim(), n=name==null?"":name.trim(), em=email==null?"":email.trim();
        if(!REG.matcher(r).matches()) throw new IllegalArgumentException("Registration number must be 4–20 letters, digits or hyphens.");
        if(n.isBlank()) throw new IllegalArgumentException("Student name is required.");
        if(event==null) throw new IllegalArgumentException("Select an event.");
        if(!EMAIL.matcher(em).matches()) throw new IllegalArgumentException("Enter a valid email address.");
        try { registrations.insert(r,n,event.id(),em); }
        catch(SQLException ex) {
            if(ex.getSQLState()!=null && ex.getSQLState().startsWith("23"))
                throw new IllegalArgumentException("Student already registered for this event.",ex);
            throw ex;
        }
    }
    public List<Registration> page(String filter,int limit,int offset) throws SQLException {
        return registrations.page(filter,limit,offset);
    }
}
