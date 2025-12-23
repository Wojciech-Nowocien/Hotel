package exceptions;

public class LoginNotFoundException extends Exception{
    private final String login;

    public LoginNotFoundException(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }
}
