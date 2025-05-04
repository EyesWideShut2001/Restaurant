package grupa.unu.restaurant.repository;

import grupa.unu.restaurant.RestaurantDb;
import grupa.unu.restaurant.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuRepository {

    public boolean addMenuItem(MenuItem item) throws SQLException {
        String sql = "INSERT INTO menu (nume, ingrediente, vegetarian, picant, pret, categorie, alcoolica) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = RestaurantDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, item.getNume());
            stmt.setString(2, item.getIngrediente());
            stmt.setBoolean(3, item.isVegetarian());
            stmt.setBoolean(4, item.isPicant());
            stmt.setDouble(5, item.getPret());
            stmt.setString(6, item.getCategorie());
            stmt.setObject(7, (item instanceof Beverage) ? ((Beverage) item).isAlcoolica() : null);
            return stmt.executeUpdate() == 1;
        }
    }

    public boolean updateMenuItem(String nume, MenuItem item) throws SQLException {
        String sql = "UPDATE menu SET ingrediente=?, vegetarian=?, picant=?, pret=?, categorie=?, alcoolica=? WHERE nume=?";
        try (Connection conn = RestaurantDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, item.getIngrediente());
            stmt.setBoolean(2, item.isVegetarian());
            stmt.setBoolean(3, item.isPicant());
            stmt.setDouble(4, item.getPret());
            stmt.setString(5, item.getCategorie());
            stmt.setObject(6, (item instanceof Beverage) ? ((Beverage) item).isAlcoolica() : null);
            stmt.setString(7, nume);
            return stmt.executeUpdate() == 1;
        }
    }

    public List<MenuItem> getAllItems() throws SQLException {
        List<MenuItem> items = new ArrayList<>();
        String sql = "SELECT * FROM menu";
        try (Connection conn = RestaurantDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String categorie = rs.getString("categorie");
                MenuItem item;
                if ("Aperitive".equals(categorie)) {
                    item = new Aperitive(rs.getString("nume"), rs.getString("ingrediente"),
                            rs.getBoolean("vegetarian"), rs.getBoolean("picant"), rs.getDouble("pret"));
                } else if ("Fel Principal".equals(categorie)) {
                    item = new MainCourse(rs.getString("nume"), rs.getString("ingrediente"),
                            rs.getBoolean("vegetarian"), rs.getBoolean("picant"), rs.getDouble("pret"));
                } else {
                    item = new Beverage(rs.getString("nume"), rs.getString("ingrediente"),
                            rs.getBoolean("alcoolica"), rs.getDouble("pret"));
                }
                items.add(item);
            }
        }
        return items;
    }
}
