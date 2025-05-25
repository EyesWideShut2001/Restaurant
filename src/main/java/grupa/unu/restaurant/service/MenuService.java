package grupa.unu.restaurant.service;

import grupa.unu.restaurant.model.MenuItem;
import grupa.unu.restaurant.repository.MenuRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class MenuService {
    private final MenuRepository menuRepository;

    public MenuService() {
        this.menuRepository = new MenuRepository();
    }

    public List<MenuItem> getAllMenuItems() {
        try {
            return menuRepository.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la încărcarea meniului", e);
        }
    }

    public List<MenuItem> getMenuItemsByCategory(String category) {
        try {
            return menuRepository.findAll().stream()
                    .filter(item -> category.equals(item.getCategory()))
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la filtrarea meniului", e);
        }
    }

    public void addMenuItem(MenuItem item) {
        try {
            menuRepository.save(item);
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la adăugarea produsului", e);
        }
    }

    public void updateMenuItem(MenuItem item) {
        try {
            menuRepository.update(item);
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea produsului", e);
        }
    }

    public void deleteMenuItem(long id) {
        try {
            menuRepository.delete(id);
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la ștergerea produsului", e);
        }
    }
} 