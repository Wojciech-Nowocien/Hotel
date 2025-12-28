import model.Admin;
import model.Client;
import model.Hotel;
import model.RoomType;
import screens.WelcomeScreen;

void main() {
    Hotel hotel = Hotel.HOTEL;
    try {
        hotel.add(new Admin("a", "a"));
        hotel.add(new Client("c", "c"));
        hotel.add(RoomType.SINGLE);
        hotel.add(RoomType.DOUBLE);
        hotel.add(RoomType.APARTMENT);
    } catch (Exception _) {
        throw new RuntimeException("Invalid fixed data!");
    }
    hotel.render(new WelcomeScreen());
}