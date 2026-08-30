package dao;

import db.DatabaseConnection;
import model.Admin;
import model.Citizen;
import model.User;

import java.sql.*;

/**
 * Data-access object for users. All JDBC lives here.
 */
public class UserDAO {

    /** Register a new Citizen. Returns generated user_id, or -1 on failure. */
    public int registerCitizen(Citizen c) {
        String sql = "INSERT INTO users (user_id, username, password, full_name, email, phone, role) " +
                     "VALUES (user_seq.NEXTVAL, ?, ?, ?, ?, ?, 'CITIZEN')";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, new String[]{"user_id"})) {

            ps.setString(1, c.getUsername());
            ps.setString(2, c.getPassword());
            ps.setString(3, c.getFullName());
            ps.setString(4, c.getEmail());
            ps.setString(5, c.getPhone());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) return keys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("registerCitizen error: " + e.getMessage());
        }
        return -1;
    }

    /** Authenticate; returns Citizen or Admin subclass depending on role. */
    public User login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("login error: " + e.getMessage());
        }
        return null;
    }

    public boolean usernameExists(String username) {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("usernameExists error: " + e.getMessage());
            return false;
        }
    }

    public boolean updateProfile(User u) {
        String sql = "UPDATE users SET full_name = ?, email = ?, phone = ? WHERE user_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, u.getFullName());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPhone());
            ps.setInt(4, u.getUserId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("updateProfile error: " + e.getMessage());
            return false;
        }
    }

    /** Polymorphic factory — returns Admin or Citizen based on role column. */
    private User mapRow(ResultSet rs) throws SQLException {
        String role = rs.getString("role");
        int id       = rs.getInt("user_id");
        String uname = rs.getString("username");
        String pw    = rs.getString("password");
        String fname = rs.getString("full_name");
        String email = rs.getString("email");
        String phone = rs.getString("phone");

        if ("ADMIN".equalsIgnoreCase(role)) {
            return new Admin(id, uname, pw, fname, email, phone);
        }
        return new Citizen(id, uname, pw, fname, email, phone);
    }
}
