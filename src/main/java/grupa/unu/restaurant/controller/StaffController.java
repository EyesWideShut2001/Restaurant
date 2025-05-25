package grupa.unu.restaurant.controller;

import grupa.unu.restaurant.model.Staff;
import grupa.unu.restaurant.repository.StaffRepository;

import java.sql.SQLException;
import java.util.List;

public class StaffController {

    private final StaffRepository staffRepository;

    public StaffController(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

//    public List<Staff> getAllStaff() throws SQLException {
//        return staffRepository.findAll();
//    }
//
//    public boolean addStaff(String username, String password, String role) throws SQLException {
//        Staff staff = new Staff(0, username, password, role);
//        return staffRepository.addStaff(staff);
//    }
//
//    public boolean deleteStaffById(int id) throws SQLException {
//        return staffRepository.deleteStaffById(id);
//    }
}