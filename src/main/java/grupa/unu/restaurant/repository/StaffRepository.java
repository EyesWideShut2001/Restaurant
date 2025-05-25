package grupa.unu.restaurant.repository;

import grupa.unu.restaurant.RestaurantDb;
import grupa.unu.restaurant.model.Staff;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StaffRepository {

    public Staff findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM staff WHERE username = ?";
        try (Connection conn = RestaurantDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Staff(
                            rs.getLong("id"),
                            rs.getString("username"),
                            rs.getString("role"),
                            rs.getString("password")
                    );
                }
            }
        }
        return null;
    }

    public List<Staff> findAll() throws SQLException {
        String sql = "SELECT * FROM staff";
        List<Staff> staffList = new ArrayList<>();

        try (Connection conn = RestaurantDb.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Staff staff = new Staff(
                    rs.getLong("id"),
                    rs.getString("username"),
                    rs.getString("role"),
                    rs.getString("password")
                );
                staffList.add(staff);
            }
        }
        return staffList;
    }

    public boolean addStaff(Staff staff) throws SQLException {
        String sql = "INSERT INTO staff (username, password, role) VALUES (?, ?, ?)";
        try (Connection conn = RestaurantDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, staff.getUsername());
            stmt.setString(2, staff.getPasswordHash());
            stmt.setString(3, staff.getRole());
            return stmt.executeUpdate() == 1;
        }
    }

    public boolean deleteStaffById(int id) throws SQLException {
        String sql = "DELETE FROM staff WHERE id = ?";
        try (Connection conn = RestaurantDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() == 1;
        }
    }

    public void save(Staff staff) throws SQLException {
        String sql = "INSERT INTO staff (username, role, password) VALUES (?, ?, ?)";

        try (Connection conn = RestaurantDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, staff.getUsername());
            stmt.setString(2, staff.getRole());
            stmt.setString(3, staff.getPasswordHash());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    staff.setId(generatedKeys.getLong(1));
                }
            }
        }
    }

    public void updatePassword(long staffId, String hashedPassword) throws SQLException {
        String sql = "UPDATE staff SET password = ? WHERE id = ?";

        try (Connection conn = RestaurantDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, hashedPassword);
            stmt.setLong(2, staffId);

            stmt.executeUpdate();
        }
    }

    public void delete(long id) throws SQLException {
        String sql = "DELETE FROM staff WHERE id = ?";

        try (Connection conn = RestaurantDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    public Connection getDbConnection() throws SQLException {
        return RestaurantDb.getConnection();
    }
}