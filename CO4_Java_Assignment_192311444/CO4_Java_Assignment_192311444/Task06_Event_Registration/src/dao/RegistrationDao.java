package app.dao;
import app.model.Registration;
import java.sql.*;
import java.util.*;
public class RegistrationDao {
    public void insert(String reg,String name,int eventId,String email) throws SQLException {
        String sql="INSERT INTO registrations(student_reg_no,student_name,event_id,email) VALUES(?,?,?,?)";
        try(Connection c=Database.getConnection(); PreparedStatement ps=c.prepareStatement(sql)) {
            ps.setString(1,reg); ps.setString(2,name); ps.setInt(3,eventId); ps.setString(4,email); ps.executeUpdate();
        }
    }
    public List<Registration> page(String filter,int limit,int offset) throws SQLException {
        String sql="SELECT r.registration_id,r.student_reg_no,r.student_name,r.event_id,e.event_name,r.email,r.registered_at " +
          "FROM registrations r JOIN events e ON e.event_id=r.event_id " +
          "WHERE (?='ALL' OR e.event_name=?) ORDER BY r.registered_at DESC LIMIT ? OFFSET ?";
        List<Registration> out=new ArrayList<>();
        try(Connection c=Database.getConnection(); PreparedStatement ps=c.prepareStatement(sql)) {
            ps.setString(1,filter); ps.setString(2,filter); ps.setInt(3,limit); ps.setInt(4,offset);
            try(ResultSet rs=ps.executeQuery()) {
                while(rs.next()) out.add(new Registration(rs.getLong("registration_id"),rs.getString("student_reg_no"),
                    rs.getString("student_name"),rs.getInt("event_id"),rs.getString("event_name"),
                    rs.getString("email"),rs.getTimestamp("registered_at").toLocalDateTime()));
            }
        }
        return out;
    }
}
