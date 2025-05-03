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
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("role")
                    );
                }
            }
        }
        return null;
    }

    public List<Staff> findAll() throws SQLException {
        List<Staff> staffList = new ArrayList<>();
        String sql = "SELECT * FROM staff";
        try (Connection conn = RestaurantDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                staffList.add(new Staff(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
                ));
            }
        }
        return staffList;
    }

    public boolean addStaff(Staff staff) throws SQLException {
        String sql = "INSERT INTO staff (username, password, role) VALUES (?, ?, ?)";
        try (Connection conn = RestaurantDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, staff.getUsername());
            stmt.setString(2, staff.getPassword());
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
}