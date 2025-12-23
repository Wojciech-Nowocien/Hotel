package exceptions;

public class InvalidPasswordException extends Exception{
    private final String login;
    private final String password;

    public InvalidPasswordException(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
