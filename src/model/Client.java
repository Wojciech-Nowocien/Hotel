package model;

import exceptions.EmptyPasswordException;

public class Client extends User {
    public Client(String login, String password) throws EmptyPasswordException {
        super(login, password);
    }

    public Client(User user) {
        super(user);
    }
}
