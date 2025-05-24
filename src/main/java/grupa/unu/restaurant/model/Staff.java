package grupa.unu.restaurant.model;

public class Staff extends User{
    private String role;

    public Staff(int id, String username, String password, String role) {
        super(id, username, password);
        this.role = role;
    }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
