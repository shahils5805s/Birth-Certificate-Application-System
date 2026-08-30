package dao;

import db.DatabaseConnection;
import model.Certificate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CertificateDAO {

    /** Issue a new certificate. Returns the generated certificate number. */
    public String issueCertificate(int applicationId, int adminId) {
        String certNo = "BC-" + System.currentTimeMillis();
        String sql = "INSERT INTO certificates (certificate_id, certificate_no, application_id, issued_by) " +
                     "VALUES (cert_seq.NEXTVAL, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, certNo);
            ps.setInt(2, applicationId);
            ps.setInt(3, adminId);
            int rows = ps.executeUpdate();
            return rows > 0 ? certNo : null;
        } catch (SQLException e) {
            System.err.println("issueCertificate error: " + e.getMessage());
            return null;
        }
    }

    public Certificate findByApplicationId(int applicationId) {
        String sql = baseSelect() + " WHERE c.application_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, applicationId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("findByApplicationId error: " + e.getMessage());
        }
        return null;
    }

    public Certificate findByCertificateNo(String certNo) {
        String sql = baseSelect() + " WHERE c.certificate_no = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, certNo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("findByCertificateNo error: " + e.getMessage());
        }
        return null;
    }

    public List<Certificate> findAll() {
        List<Certificate> list = new ArrayList<>();
        String sql = baseSelect() + " ORDER BY c.issued_at DESC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("findAll error: " + e.getMessage());
        }
        return list;
    }

    public Certificate findByUserId(int userId) {
        String sql = baseSelect() +
                " WHERE a.user_id = ? AND a.status = 'APPROVED'" +
                " ORDER BY c.issued_at DESC FETCH FIRST 1 ROWS ONLY";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("findByUserId error: " + e.getMessage());
        }
        return null;
    }

    // --- helpers ---

    private String baseSelect() {
        return "SELECT c.certificate_id, c.certificate_no, c.application_id, c.issued_by, c.issued_at, " +
               "a.child_name, a.father_name, a.mother_name, a.date_of_birth, a.place_of_birth " +
               "FROM certificates c JOIN applications a ON c.application_id = a.application_id";
    }

    private Certificate mapRow(ResultSet rs) throws SQLException {
        Certificate c = new Certificate();
        c.setCertificateId(rs.getInt("certificate_id"));
        c.setCertificateNo(rs.getString("certificate_no"));
        c.setApplicationId(rs.getInt("application_id"));
        c.setIssuedBy(rs.getInt("issued_by"));
        c.setIssuedAt(rs.getTimestamp("issued_at"));
        c.setChildName(rs.getString("child_name"));
        c.setFatherName(rs.getString("father_name"));
        c.setMotherName(rs.getString("mother_name"));
        Date dob = rs.getDate("date_of_birth");
        c.setDateOfBirth(dob == null ? "" : dob.toString());
        c.setPlaceOfBirth(rs.getString("place_of_birth"));
        return c;
    }
}
