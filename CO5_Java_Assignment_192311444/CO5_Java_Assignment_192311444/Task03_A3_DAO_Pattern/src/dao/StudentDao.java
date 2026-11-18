package dao;
import model.Student;
import java.sql.*;
import java.util.*;

public class StudentDao implements Dao<Student, Integer> {
    private final Connection connection;
    public StudentDao(Connection connection) { this.connection = Objects.requireNonNull(connection); }

    @Override public Integer insert(Student s) throws SQLException {
        String sql = "INSERT INTO student(register_no, student_name, email) VALUES(?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, s.registerNumber()); ps.setString(2, s.name()); ps.setString(3, s.email());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }
        throw new SQLException("Insert did not return a generated key");
    }

    @Override public Optional<Student> findById(Integer id) throws SQLException {
        String sql = "SELECT student_id,register_no,student_name,email FROM student WHERE student_id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        }
    }
    @Override public List<Student> findAll() throws SQLException {
        List<Student> out = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT student_id,register_no,student_name,email FROM student ORDER BY student_id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(map(rs));
        }
        return out;
    }
    @Override public boolean update(Student s) throws SQLException {
        String sql = "UPDATE student SET register_no=?,student_name=?,email=? WHERE student_id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1,s.registerNumber()); ps.setString(2,s.name());
            ps.setString(3,s.email()); ps.setInt(4,s.id());
            return ps.executeUpdate() == 1;
        }
    }
    @Override public boolean delete(Integer id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM student WHERE student_id=?")) {
            ps.setInt(1,id); return ps.executeUpdate() == 1;
        }
    }
    private Student map(ResultSet rs) throws SQLException {
        return new Student(rs.getInt("student_id"), rs.getString("register_no"),
                rs.getString("student_name"), rs.getString("email"));
    }
}
