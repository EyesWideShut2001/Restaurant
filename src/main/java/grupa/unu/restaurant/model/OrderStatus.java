package grupa.unu.restaurant.model;

public enum OrderStatus {
    PENDING("În așteptare"),
    APPROVED("Aprobată"),
    REJECTED("Respinsă"),
    IN_PREPARATION("În preparare"),
    READY("Gata"),
    DELIVERED("Livrată");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
