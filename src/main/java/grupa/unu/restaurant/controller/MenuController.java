package grupa.unu.restaurant.controller;

import grupa.unu.restaurant.model.*;
import grupa.unu.restaurant.repository.MenuRepository;

import java.sql.SQLException;
import java.util.List;

public class MenuController {

    private final MenuRepository repo;

    public MenuController(MenuRepository repo) {
        this.repo = repo;
    }

    public boolean adaugaProdus(MenuItem item) throws SQLException {
        return repo.addMenuItem(item);
    }

    public boolean actualizeazaProdus(String numeInitial, MenuItem item) throws SQLException {
        return repo.updateMenuItem(numeInitial, item);
    }

    public List<MenuItem> getAllProduse() throws SQLException {
        return repo.getAllItems();
    }
}
