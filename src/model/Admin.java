package model;

import exceptions.EmptyPasswordException;

public class Admin extends User {
    public Admin(String login, String password) throws EmptyPasswordException {
        super(login, password);
    }

    public Admin(User user) {
        super(user);
    }
}
