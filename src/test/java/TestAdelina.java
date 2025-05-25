import grupa.unu.restaurant.model.Order;
import grupa.unu.restaurant.model.OrderItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

// Clasa de teste pentru Order
class TestAdelina {
    private Order order;

    // Initializare inainte de fiecare test
    @BeforeEach
    void setUp() {
        order = new Order();
    }

    // Testeaza daca pretul total initial e zero
    @Test
    void testNewOrderHasZeroTotalPrice() {
        assertEquals(0.0, order.getTotalPrice(), "Comanda noua trebuie sa aiba pret zero");
    }

    // Testeaza calculul pretului total
    @Test
    void testTotalPriceCalculation() {
        ObservableList<OrderItem> items = FXCollections.observableArrayList();
        items.add(new OrderItem("Pizza", 25.0, 2));
        items.add(new OrderItem("Suc", 5.0, 3));
        order.setItems(items);

        assertEquals(65.0, order.getTotalPrice(), "Pretul total trebuie sa fie suma (pret * cantitate)");
    }

    // Testeaza timpul comenzii
    @Test
    void testOrderTimeManagement() {
        LocalDateTime now = LocalDateTime.now();
        order.setOrderTime(now);

        assertEquals(now, order.getOrderTime(), "Timpul comenzii trebuie salvat corect");
    }

    // Testeaza statusul comenzii
    @Test
    void testOrderStatus() {
        String status = "PENDING";
        order.setStatus(status);

        assertEquals(status, order.getStatus(), "Status-ul comenzii trebuie salvat corect");
    }

    // Testeaza timpul estimat
    @Test
    void testEstimatedTimeProperty() {
        int estimatedTime = 30;
        order.setEstimatedTime(estimatedTime);

        assertEquals(estimatedTime, order.getEstimatedTime(), "Timpul estimat trebuie salvat corect");
        assertEquals(estimatedTime, order.estimatedTimeProperty().get(), "Proprietatea de timp estimat trebuie sa fie corecta");
    }

    // Testeaza metoda toString
    @Test
    void testToString() {
        order.setId(1L);
        order.setStatus("PENDING");
        ObservableList<OrderItem> items = FXCollections.observableArrayList(
            new OrderItem("Pizza", 25.0, 2)
        );
        order.setItems(items);

        String expected = "Order #1 - PENDING";
    }

    // Testeaza proprietatile initiale
    @Test
    void testEmptyOrderProperties() {
        assertNull(order.getApprovalTime(), "Timpul de aprobare initial trebuie sa fie null");
        assertNull(order.getApprovedBy(), "Aprobat de trebuie sa fie initial null");
        assertEquals("", order.getNotes() == null ? "" : order.getNotes(), "Notele initiale trebuie sa fie goale");
        assertNull(order.getItems(), "Lista de items initiala trebuie sa fie null");
    }

}