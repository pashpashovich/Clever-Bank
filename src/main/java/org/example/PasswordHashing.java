package org.example;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * The class of hashing password
 */
public class PasswordHashing {
    /**
     * The method that generates the random "salt"
     * @return returns "salt"
     */
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     *
     * @param password - the password that was entered by user
     * @param salt - generated "salt"
     * @return returns the hashed password
     * @throws NoSuchAlgorithmException - the exception which is thrown when there is no algorithm to hash the password
     */
    public static String hashPassword(String password,String salt) throws NoSuchAlgorithmException {
        String valueToHash = password + salt;
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashedBytes = md.digest(valueToHash.getBytes());
        return Base64.getEncoder().encodeToString(hashedBytes);
    }
}
