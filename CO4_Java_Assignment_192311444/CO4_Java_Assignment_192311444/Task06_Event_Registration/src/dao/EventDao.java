package app.dao;
import app.model.Event;
import java.sql.*;
import java.util.*;
public class EventDao {
    public List<Event> findAll() throws SQLException {
        List<Event> out=new ArrayList<>();
        try(Connection c=Database.getConnection();
            PreparedStatement ps=c.prepareStatement("SELECT event_id,event_name FROM events WHERE active=TRUE ORDER BY event_name");
            ResultSet rs=ps.executeQuery()) {
            while(rs.next()) out.add(new Event(rs.getInt(1),rs.getString(2)));
        }
        return out;
    }
}
