
import grupa.unu.restaurant.model.MenuItem;
import grupa.unu.restaurant.repository.MenuRepository;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestCristian {

    private static MenuRepository menuRepository;

    @BeforeAll
    static void setup() {
        menuRepository = new MenuRepository();
    }

    @BeforeEach
    void clearMenuTable() throws SQLException {
        try (var conn = grupa.unu.restaurant.RestaurantDb.getConnection();
             var stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM menu");
        }
    }

    @Test
    @Order(1)
    void testSaveMenuItem() throws SQLException {
        MenuItem item = new MenuItem(0, "Pizza Margherita", "Pizza", 25.0, "faina, rosii, mozzarella", true, false, false);
        menuRepository.save(item);
        assertTrue(item.getId() > 0, "ID-ul ar trebui sa fie setat după salvare");
    }

    @Test
    @Order(2)
    void testFindAllAfterSave() throws SQLException {
        MenuItem item = new MenuItem(0, "Pizza Margherita", "Pizza", 25.0, "faina, rosii, mozzarella", true, false, false);
        menuRepository.save(item);

        List<MenuItem> items = menuRepository.findAll();
        assertFalse(items.isEmpty(), "Lista de meniuri nu ar trebui sa fie goala dupa salvare");
        boolean found = items.stream().anyMatch(i -> "Pizza Margherita".equals(i.getName()));
        assertTrue(found, "Ar trebui sa gaseasca Pizza Margherita in meniu");
    }

    @Test
    @Order(3)
    void testSaveMultipleMenuItems() throws SQLException {
        MenuItem item1 = new MenuItem(0, "Supa de legume", "Supa", 15.0, "legume, condimente", true, false, false);
        MenuItem item2 = new MenuItem(0, "Burger Vita", "Burger", 32.0, "vita, chifla, salata", false, false, false);
        menuRepository.save(item1);
        menuRepository.save(item2);

        List<MenuItem> items = menuRepository.findAll();
        assertTrue(items.stream().anyMatch(i -> "Supa de legume".equals(i.getName())));
        assertTrue(items.stream().anyMatch(i -> "Burger Vita".equals(i.getName())));
    }

    @Test
    @Order(4)
    void testFindAllReturnsCorrectFields() throws SQLException {
        MenuItem item = new MenuItem(0, "Pizza Margherita", "Pizza", 25.0, "faina, rosii, mozzarella", true, false, false);
        menuRepository.save(item);

        List<MenuItem> items = menuRepository.findAll();
        MenuItem foundItem = items.stream().filter(i -> "Pizza Margherita".equals(i.getName())).findFirst().orElse(null);
        assertNotNull(foundItem);
        assertEquals("Pizza", foundItem.getCategory());
        assertEquals(25.0, foundItem.getPrice());
        assertTrue(foundItem.isVegetarian());
    }

    @Test
    @Order(5)
    void testSaveMenuItemWithSpecialCharacters() throws SQLException {
        MenuItem item = new MenuItem(0, "Ciorba de burta", "Supa", 22.5, "burta, smantana, ou", false, false, false);
        menuRepository.save(item);

        List<MenuItem> items = menuRepository.findAll();
        boolean found = items.stream().anyMatch(i -> "Ciorba de burta".equals(i.getName()));
        assertTrue(found, "Ar trebui sa gaseasca Ciorba de burta în meniu");
    }
}
