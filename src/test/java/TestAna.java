import grupa.unu.restaurant.service.PasswordHasher;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestAna {


    @Test
    public void testHashPasswordReturnsNonNullAndFormattedHash() {
        String password = "mySecret123";
        String hashed = PasswordHasher.hashPassword(password);

        assertNotNull(hashed, "Hashed password should not be null");
        // The hash should contain the salt and a '$' separator
        assertTrue(hashed.contains("$"), "Hashed password should contain '$' separator");
        String[] parts = hashed.split("\\$");
        assertEquals(2, parts.length, "Hashed password should have two parts separated by '$'");
        assertEquals(parts[0], "XVuH0cKCqBP/GWJ5pX5dXQ==", "Salt part should match the fixed salt");
        assertFalse(parts[1].isEmpty(), "Hashed password part should not be empty");
    }

    @Test
    public void testCheckPasswordReturnsTrueForCorrectPassword() {
        String password = "correctPassword!";
        String hashed = PasswordHasher.hashPassword(password);

        assertTrue(PasswordHasher.checkPassword(password, hashed), "checkPassword should return true for the correct password");
    }

    @Test
    public void testCheckPasswordReturnsFalseForIncorrectPassword() {
        String correctPassword = "correctPassword!";
        String incorrectPassword = "wrongPassword!";
        String hashed = PasswordHasher.hashPassword(correctPassword);

        assertFalse(PasswordHasher.checkPassword(incorrectPassword, hashed), "checkPassword should return false for an incorrect password");
    }

    @Test
    public void testCheckPasswordReturnsFalseForInvalidHashFormat() {
        String password = "anything";
        String invalidHash = "invalidHashWithoutSeparator";

        assertFalse(PasswordHasher.checkPassword(password, invalidHash), "checkPassword should return false for invalid hash format");
    }

    @Test
    public void testCheckPasswordReturnsFalseForSaltMismatch() {
        String password = "password123";
        // Manually create a hash with a different salt prefix
        String wrongSaltHash = "WrongSalt==" + "$" + PasswordHasher.hashPassword(password).split("\\$")[1];

        assertFalse(PasswordHasher.checkPassword(password, wrongSaltHash), "checkPassword should return false if salt mismatches");
    }


}
