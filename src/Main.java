import model.Hotel;
import screens.WelcomeScreen;

void main() {
    Hotel hotel = new Hotel();
    WelcomeScreen screen = new WelcomeScreen(hotel);
    hotel.render(screen);
}