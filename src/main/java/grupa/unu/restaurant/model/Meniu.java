package grupa.unu.restaurant.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Meniu {
    private List<MenuItem> items;

    public Meniu() {
        this.items = new ArrayList<>();
    }

    public Map<String, List<MenuItem>> getItemsByCategory() {
        return items.stream()
                .collect(Collectors.groupingBy(MenuItem::getCategory));
    }

    public void addItem(MenuItem item) {
        items.add(item);
    }

    public List<MenuItem> getItems() {
        return items;
    }

    // Metodă pentru staff: setează disponibilitatea unui produs
    public void setDisponibilitateProdus(String numeProdus, boolean disponibil) {
        for (MenuItem item : items) {
            if (item.getName().equalsIgnoreCase(numeProdus)) {
                item.setAvailable(disponibil);
                break;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Map<String, List<MenuItem>> categorizedItems = getItemsByCategory();

        for (Map.Entry<String, List<MenuItem>> entry : categorizedItems.entrySet()) {
            sb.append("\n").append(entry.getKey()).append(":\n");
            for (MenuItem item : entry.getValue()) {
                sb.append(item.getName())
                        .append(" - ")
                        .append(item.getPrice())
                        .append(" lei - ")
                        .append(item.isAvailable() ? "Disponibil" : "Indisponibil")
                        .append("\n");
            }
        }

        return sb.toString();
    }
}
