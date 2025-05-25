
package grupa.unu.restaurant.repository;

import grupa.unu.restaurant.RestaurantDb;
import grupa.unu.restaurant.model.MenuItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static grupa.unu.restaurant.RestaurantDb.getConnection;

public class MenuRepository {
    public List<MenuItem> findAll() throws SQLException {
        String sql = "SELECT * FROM menu";
        List<MenuItem> items = new ArrayList<>();

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                MenuItem item = new MenuItem(
                        rs.getLong("id"),
                        rs.getString("nume"),
                        rs.getString("categorie"),
                        rs.getDouble("pret"),
                        rs.getString("ingrediente"),
                        rs.getBoolean("vegetarian"),
                        rs.getBoolean("picant"),
                        rs.getBoolean("alcoolica")
                );
                item.setAvailable(rs.getBoolean("available"));
                items.add(item);
            }
        }
        return items;
    }

    public void save(MenuItem item) throws SQLException {
        String sql = "INSERT INTO menu (nume, categorie, pret, ingrediente, vegetarian, picant, alcoolica, available) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, item.getName());
            stmt.setString(2, item.getCategory());
            stmt.setDouble(3, item.getPrice());
            stmt.setString(4, item.getIngredients());
            stmt.setBoolean(5, item.isVegetarian());
            stmt.setBoolean(6, item.isSpicy());
            stmt.setBoolean(7, item.isAlcoholic());
            stmt.setBoolean(8, item.isAvailable());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    item.setId(generatedKeys.getLong(1));
                }
            }
        }
    }

    public void update(MenuItem item) throws SQLException {
        String sql = "UPDATE menu SET nume = ?, categorie = ?, pret = ?, ingrediente = ?, " +
                "vegetarian = ?, picant = ?, alcoolica = ?, available = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, item.getName());
            stmt.setString(2, item.getCategory());
            stmt.setDouble(3, item.getPrice());
            stmt.setString(4, item.getIngredients());
            stmt.setBoolean(5, item.isVegetarian());
            stmt.setBoolean(6, item.isSpicy());
            stmt.setBoolean(7, item.isAlcoholic());
            stmt.setBoolean(8, item.isAvailable());
            stmt.setLong(9, item.getId());

            stmt.executeUpdate();
        }
    }

    public void delete(long id) throws SQLException {
        String sql = "DELETE FROM menu WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    public void updateMenuItemAvailability(long menuItemId, boolean available) {
        String sql = "UPDATE menu SET available = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, available);
            stmt.setLong(2, menuItemId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
