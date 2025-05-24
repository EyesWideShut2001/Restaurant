package grupa.unu.restaurant.controller;

import grupa.unu.restaurant.model.Manager;
import grupa.unu.restaurant.model.Staff;
import grupa.unu.restaurant.repository.ManagerRepository;
import grupa.unu.restaurant.repository.StaffRepository;

public class AuthController {

    private final ManagerRepository managerRepository;
    private final StaffRepository staffRepository;

    public enum AuthResultType { MANAGER, STAFF, NONE }

    public static class AuthResult {
        private final AuthResultType type;
        private final Manager manager;
        private final Staff staff;

        public AuthResult(AuthResultType type, Manager manager, Staff staff) {
            this.type = type;
            this.manager = manager;
            this.staff = staff;
        }

        public AuthResultType getType() { return type; }
        public Manager getManager() { return manager; }
        public Staff getStaff() { return staff; }
    }

    public AuthController(ManagerRepository managerRepository, StaffRepository staffRepository) {
        this.managerRepository = managerRepository;
        this.staffRepository = staffRepository;
    }

    /**
     * Încearcă autentificarea ca manager sau staff.
     * @return AuthResult cu tipul și obiectul autentificat, sau NONE dacă nu există.
     */
    public AuthResult authenticate(String username, String password) {
        try {
            Manager manager = managerRepository.findByUsername(username);
            if (manager != null && manager.getPassword().equals(password)) {
                return new AuthResult(AuthResultType.MANAGER, manager, null);
            }
            Staff staff = staffRepository.findByUsername(username);
            if (staff != null && staff.getPassword().equals(password)) {
                return new AuthResult(AuthResultType.STAFF, null, staff);
            }
        } catch (Exception e) {
            // Poți loga eroarea dacă vrei
        }
        return new AuthResult(AuthResultType.NONE, null, null);
    }
}