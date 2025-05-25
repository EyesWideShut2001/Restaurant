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


}