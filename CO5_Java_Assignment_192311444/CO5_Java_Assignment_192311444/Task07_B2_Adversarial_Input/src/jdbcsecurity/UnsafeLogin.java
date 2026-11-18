package jdbcsecurity;
import java.sql.*;
public class UnsafeLogin {
    public static boolean authenticate(Connection con,String id,String pwd) throws SQLException {
        String sql="SELECT 1 FROM users WHERE id='"+id+"' AND pwd='"+pwd+"'";
        try(Statement st=con.createStatement();ResultSet rs=st.executeQuery(sql)){return rs.next();}
    }
}
