import model.Hotel;
import screens.WelcomeScreen;

public class Main {
    static void main() {
        Hotel hotel = new Hotel();
        WelcomeScreen screen = new WelcomeScreen(hotel);
        hotel.render(screen);
    }
}