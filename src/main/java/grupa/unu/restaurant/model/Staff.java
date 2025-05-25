package grupa.unu.restaurant.model;

import javafx.beans.property.*;

public class Staff {
    private final LongProperty id;
    private final StringProperty username;
    private final StringProperty role;
    private String passwordHash;

    public Staff() {
        this.id = new SimpleLongProperty();
        this.username = new SimpleStringProperty();
        this.role = new SimpleStringProperty();
    }

    public Staff(long id, String username, String role, String passwordHash) {
        this.id = new SimpleLongProperty(id);
        this.username = new SimpleStringProperty(username);
        this.role = new SimpleStringProperty(role);
        this.passwordHash = passwordHash;
    }

    // Getters and setters
    public long getId() { return id.get(); }
    public void setId(long value) { id.set(value); }
    public LongProperty idProperty() { return id; }

    public String getUsername() { return username.get(); }
    public void setUsername(String value) { username.set(value); }
    public StringProperty usernameProperty() { return username; }

    public String getRole() { return role.get(); }
    public void setRole(String value) { role.set(value); }
    public StringProperty roleProperty() { return role; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String value) { this.passwordHash = value; }
}
