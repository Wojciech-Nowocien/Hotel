package managers;

import exceptions.DuplicateLoginException;
import exceptions.InvalidPasswordException;
import exceptions.LoginNotFoundException;
import model.User;

import java.util.ArrayList;

public class UserManager {
    private final ArrayList<User> users;
    private User currentUser;

    public UserManager() {
        this.users = new ArrayList<>();
        this.currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void add(User user) throws DuplicateLoginException {
        var usersWithSameLogin = users.stream().filter(u -> u.getLogin().equals(user.getLogin())).toList();

        if (!usersWithSameLogin.isEmpty()) {
            throw new DuplicateLoginException();
        }

        users.add(user);
    }

    public void login(String login, String password) throws LoginNotFoundException, InvalidPasswordException {
        currentUser = null;
        var usersToLogin = users.stream().filter(u -> u.getLogin().equals(login)).toList();

        if (usersToLogin.isEmpty()) {
            throw new LoginNotFoundException(login);
        }

        User user = usersToLogin.getFirst();

        if (!user.login(password)) {
            throw new InvalidPasswordException();
        }

        currentUser = user;
    }
}
