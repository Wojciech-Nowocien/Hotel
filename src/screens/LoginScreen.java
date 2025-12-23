package screens;

import managers.UserManager;
import model.Hotel;

public class LoginScreen extends Screen {
    private final UserManager users;

    public LoginScreen(Hotel hotel) {
        super(hotel);
        this.users = hotel.getUsers();
    }

    @Override
    public void render() {

    }
}
