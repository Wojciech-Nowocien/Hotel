package screens;

import managers.UserManager;
import model.Hotel;

public class RegisterScreen extends Screen {
    private final UserManager users;

    public RegisterScreen(Hotel hotel) {
        super(hotel);
        this.users = hotel.getUsers();
    }

    @Override
    public void render() {

    }
}
