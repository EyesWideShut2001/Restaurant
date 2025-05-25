package grupa.unu.restaurant.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.logging.Logger;
import java.util.logging.Level;

public class PasswordHasher {
    private static final Logger logger = Logger.getLogger(PasswordHasher.class.getName());
    private static final String FIXED_SALT = "XVuH0cKCqBP/GWJ5pX5dXQ=="; // Using a fixed salt for development
    
    public static String hashPassword(String password) {
        try {
            byte[] salt = Base64.getDecoder().decode(FIXED_SALT);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes());
            String hashedStr = Base64.getEncoder().encodeToString(hashedPassword);
            
            return FIXED_SALT + "$" + hashedStr;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Eroare la criptarea parolei", e);
        }
    }
    
    public static boolean checkPassword(String password, String storedHash) {
        logger.info("Checking password. Stored hash: " + storedHash);
        
        String[] parts = storedHash.split("\\$");
        if (parts.length != 2) {
            logger.warning("Invalid hash format - does not contain exactly one '$' separator");
            return false;
        }
        
        String saltStr = parts[0];
        if (!FIXED_SALT.equals(saltStr)) {
            logger.warning("Salt mismatch");
            return false;
        }
        
        String hashedPassword = hashPassword(password);
        
        logger.info("Generated hash: " + hashedPassword);
        logger.info("Stored hash: " + storedHash);
        
        return hashedPassword.equals(storedHash);
    }

    // Test method to generate a hash for a given password
    public static void main(String[] args) {
        String password = "adminRestaurantMagic12";
        String hashedPassword = hashPassword(password);
        System.out.println("Generated hash: " + hashedPassword);
        System.out.println("Hash verification: " + checkPassword(password, hashedPassword));
    }
} 