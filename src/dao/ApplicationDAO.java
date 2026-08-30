package dao;

import db.DatabaseConnection;
import model.Application;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ApplicationDAO {

    public int submitApplication(Application a) {
        String sql = "INSERT INTO applications (application_id, user_id, child_name, gender, " +
                     "date_of_birth, place_of_birth, father_name, mother_name, address, status) " +
                     "VALUES (app_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, 'PENDING')";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, new String[]{"application_id"})) {

            ps.setInt(1, a.getUserId());
            ps.setString(2, a.getChildName());
            ps.setString(3, a.getGender());
            ps.setDate(4, a.getDateOfBirth());
            ps.setString(5, a.getPlaceOfBirth());
            ps.setString(6, a.getFatherName());
            ps.setString(7, a.getMotherName());
            ps.setString(8, a.getAddress());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) return keys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("submitApplication error: " + e.getMessage());
        }
        return -1;
    }

    public List<Application> findByUser(int userId) {
        return runQuery("SELECT * FROM applications WHERE user_id = ? ORDER BY submitted_at DESC",
                        ps -> ps.setInt(1, userId));
    }

    public List<Application> findAll() {
        return runQuery("SELECT * FROM applications ORDER BY submitted_at DESC", ps -> {});
    }

    public List<Application> findByStatus(String status) {
        return runQuery("SELECT * FROM applications WHERE status = ? ORDER BY submitted_at DESC",
                        ps -> ps.setString(1, status));
    }

    public Application findById(int applicationId) {
        String sql = "SELECT * FROM applications WHERE application_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, applicationId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("findById error: " + e.getMessage());
        }
        return null;
    }

    public boolean updateStatus(int applicationId, String status, String remarks) {
        String sql = "UPDATE applications SET status = ?, remarks = ? WHERE application_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, remarks);
            ps.setInt(3, applicationId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("updateStatus error: " + e.getMessage());
            return false;
        }
    }

    // --- helpers ---

    @FunctionalInterface
    private interface StmtSetter { void set(PreparedStatement ps) throws SQLException; }

    private List<Application> runQuery(String sql, StmtSetter setter) {
        List<Application> list = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            setter.set(ps);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("query error: " + e.getMessage());
        }
        return list;
    }

    private Application mapRow(ResultSet rs) throws SQLException {
        return new Application(
                rs.getInt("application_id"),
                rs.getInt("user_id"),
                rs.getString("child_name"),
                rs.getString("gender"),
                rs.getDate("date_of_birth"),
                rs.getString("place_of_birth"),
                rs.getString("father_name"),
                rs.getString("mother_name"),
                rs.getString("address"),
                rs.getString("status"),
                rs.getTimestamp("submitted_at"),
                rs.getString("remarks")
        );
    }
}
