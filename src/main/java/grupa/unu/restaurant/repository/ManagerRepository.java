package grupa.unu.restaurant.repository;

import grupa.unu.restaurant.RestaurantDb;
import grupa.unu.restaurant.model.Manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManagerRepository {

    public Manager findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM manager WHERE username = ?";
        try (Connection conn = RestaurantDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Manager(
                            rs.getLong("id"),
                            rs.getString("username"),
                            rs.getString("password")  // This is the hashed password from the database
                    );
                }
            }
        }
        return null;
    }

    // Nu există operații de adăugare/ștergere pentru manager (doar unul implicit)
}
