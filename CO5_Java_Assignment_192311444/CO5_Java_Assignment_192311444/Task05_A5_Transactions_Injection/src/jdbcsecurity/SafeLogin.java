package jdbcsecurity;
import java.sql.*;

public class SafeLogin {
    public static boolean authenticate(Connection con, String id, String password) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE id=? AND pwd=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1,id); ps.setString(2,password);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }
}
