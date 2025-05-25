package grupa.unu.restaurant.service;

import grupa.unu.restaurant.model.Staff;
import grupa.unu.restaurant.repository.StaffRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class StaffService {
    private final StaffRepository staffRepository;

    public StaffService() {
        this.staffRepository = new StaffRepository();
    }

    public List<Staff> getAllStaff() {
        try {
            return staffRepository.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la încărcarea personalului", e);
        }
    }

    public List<Staff> getStaffByRole(String role) {
        try {
            return staffRepository.findAll().stream()
                    .filter(staff -> role.equals(staff.getRole()))
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la filtrarea personalului", e);
        }
    }

    public void addStaff(Staff staff, String plainPassword) {
        try {
            String hashedPassword = PasswordHasher.hashPassword(plainPassword);
            staff.setPasswordHash(hashedPassword);
            staffRepository.save(staff);
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la adăugarea angajatului", e);
        }
    }

    public void resetPassword(long staffId) {
        try {
            // Generează o parolă temporară
            String tempPassword = generateTempPassword();
            String hashedPassword = PasswordHasher.hashPassword(tempPassword);
            
            staffRepository.updatePassword(staffId, hashedPassword);
            // TODO: Trimite parola temporară prin email sau alt canal securizat
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la resetarea parolei", e);
        }
    }

    public void deleteStaff(long id) {
        try {
            staffRepository.delete(id);
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la ștergerea angajatului", e);
        }
    }

    private String generateTempPassword() {
        // Generează o parolă temporară de 12 caractere
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            int index = (int) (chars.length() * Math.random());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }
} 