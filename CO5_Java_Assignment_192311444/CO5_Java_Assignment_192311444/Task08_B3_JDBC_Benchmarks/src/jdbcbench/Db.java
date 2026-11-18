package jdbcbench;
import java.sql.*;
public final class Db {
    private Db(){}
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(
            System.getenv().getOrDefault("DB_URL","jdbc:mysql://localhost:3306/co5_bus_pass"),
            System.getenv().getOrDefault("DB_USER","root"),
            System.getenv().getOrDefault("DB_PASSWORD",""));
    }
}
