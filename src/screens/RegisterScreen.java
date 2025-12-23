package screens;

import managers.UserManager;

public class RegisterScreen extends Screen {
    private final UserManager users;

    public RegisterScreen() {
        this.users = hotel.getUsers();
    }

    @Override
    public void render() {

    }
}
