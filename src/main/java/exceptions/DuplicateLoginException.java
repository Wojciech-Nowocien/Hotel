package exceptions;

public class DuplicateLoginException extends Exception{
    private final String login;

    public DuplicateLoginException(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }
}
