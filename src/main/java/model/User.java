package model;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {
    private final String login;
    private final String passwordHash;

    public User(String login, String password) {
        this.login = login;
        this.passwordHash = hash(password);
    }

    public String getLogin() {
        return login;
    }

    private String hash(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(text.getBytes(StandardCharsets.UTF_8));

            return new BigInteger(1, bytes).toString(16);
        } catch (NoSuchAlgorithmException e) {
            return text;
        }
    }

    public boolean login(String password) {
        return hash(password).equals(passwordHash);
    }
}
