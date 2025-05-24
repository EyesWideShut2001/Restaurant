package grupa.unu.restaurant.model;

import java.util.*;

public class Meniu {
    private List<MenuItem> produse = new ArrayList<>();

    public void adaugaProdus(MenuItem item) {
        produse.add(item);
    }

    public void afiseazaMeniu() {
        Map<String, List<MenuItem>> categorii = new HashMap<>();
        for (MenuItem item : produse) {
            categorii.computeIfAbsent(item.getCategorie(), k -> new ArrayList<>()).add(item);
        }

        for (String categorie : categorii.keySet()) {
            System.out.println("=== " + categorie + " ===");
            for (MenuItem item : categorii.get(categorie)) {
                System.out.println(item.getNume() + " - " + item.getPret() + " lei");
            }
        }
    }
}
