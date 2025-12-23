import model.Hotel;
import screens.WelcomeScreen;

void main() {
    Hotel hotel = Hotel.HOTEL;
    hotel.render(new WelcomeScreen());
}