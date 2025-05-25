import grupa.unu.restaurant.model.Staff;
import grupa.unu.restaurant.repository.StaffRepository;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestAlin {

    private static StaffRepository staffRepository;

    @BeforeAll
    static void setup() {
        staffRepository = new StaffRepository();
    }

    @BeforeEach
    void clearStaffTable() throws SQLException {
        try (Connection conn = staffRepository.getDbConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM staff");
        }
    }

    @Test
    @Order(1)
    void testFindByUsername_existingUser() throws SQLException {
        Staff staff = new Staff(0, "john_doe", "admin", "secret");
        staffRepository.save(staff);

        Staff found = staffRepository.findByUsername("john_doe");

        assertNotNull(found);
        assertEquals("john_doe", found.getUsername());
    }

    @Test
    @Order(2)
    void testFindByUsername_nonExistentUser() throws SQLException {
        Staff found = staffRepository.findByUsername("non_existent_user");
        assertNull(found);
    }

    @Test
    @Order(3)
    void testFindByUsername_multipleUsersSamePrefix() throws SQLException {
        staffRepository.save(new Staff(0, "alex", "chef", "pass1"));
        staffRepository.save(new Staff(0, "alexander", "cashier", "pass2"));

        Staff found = staffRepository.findByUsername("alex");

        assertNotNull(found);
        assertEquals("alex", found.getUsername());
    }

    @Test
    @Order(4)
    void testSave_staffWithSimpleData() throws SQLException {
        Staff s = new Staff(0, "user1", "admin", "pw1");
        staffRepository.save(s);
        assertTrue(s.getId() > 0);
    }


    @Test
    @Order(5)
    void testSave_duplicateUsername() throws SQLException {
        Staff s1 = new Staff(0, "unique_user", "admin", "pw1");
        Staff s2 = new Staff(0, "unique_user", "cashier", "pw2");

        staffRepository.save(s1);
        Exception exception = assertThrows(SQLException.class, () -> {
            staffRepository.save(s2);
        });
    }

    @Test
    @Order(7)
    void testAddStaff_successfulInsert() throws SQLException {
        Staff staff = new Staff(0, "diana", "admin", "diana123");
        boolean result = staffRepository.addStaff(staff);
        assertTrue(result);
    }

    @Test
    @Order(8)
    void testAddStaff_duplicateUsernameFails() throws SQLException {
        Staff staff1 = new Staff(0, "maria", "chef", "pw1");
        Staff staff2 = new Staff(0, "maria", "cashier", "pw2");

        staffRepository.addStaff(staff1);
        boolean exceptionThrown = false;

        try {
            staffRepository.addStaff(staff2);
        } catch (SQLException e) {
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown, "Adding duplicate username should fail.");
    }

    @Test
    @Order(9)
    void testDelete_existingStaff() throws SQLException {
        Staff staff = new Staff(0, "todelete", "chef", "pw");
        staffRepository.save(staff);

        staffRepository.delete(staff.getId());

        Staff deleted = staffRepository.findByUsername("todelete");
        assertNull(deleted);
    }

    @Test
    @Order(10)
    void testDelete_nonexistentId() throws SQLException {
        staffRepository.delete(12345);
    }

    @Test
    @Order(11)
    void testDelete_twiceSameId() throws SQLException {
        Staff staff = new Staff(0, "twicedelete", "admin", "pw");
        staffRepository.save(staff);

        staffRepository.delete(staff.getId());
        staffRepository.delete(staff.getId());
    }

}
