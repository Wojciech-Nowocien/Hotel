package model;

import exceptions.EmptyPasswordException;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class User {
    private final String login;
    private final String passwordHash;

    public User(String login, String password) throws EmptyPasswordException {
        this.login = login;
        if (password.isEmpty()) throw new EmptyPasswordException();
        this.passwordHash = hash(password);
    }

    public User(User other) {
        this.login = other.login;
        this.passwordHash = other.passwordHash;
    }

    public String getLogin() {
        return login;
    }

    @Override
    public String toString() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof User)) return false;
        User other = (User) o;
        return Objects.equals(login, other.login);
    }

    @Override
    public int hashCode() {
        return login != null ? login.hashCode() : 0;
    }
}
