import events.enums.PaymentMethod;
import model.Admin;
import model.Client;
import model.Hotel;
import model.RoomType;
import screens.WelcomeScreen;

void main() {
    Hotel hotel = Hotel.HOTEL;

    try {
        hotel.add(new Admin("adam", "hasłoadam"));
        hotel.add(new Admin("ewa", "hasłolewa"));
        hotel.add(new Admin("ola", "hasłoola"));

        hotel.add(new Client("jan", "hasłojan"));
        hotel.add(new Client("anna", "hasłoanna"));
        hotel.add(new Client("piotr", "hasłopiotr"));
        hotel.add(new Client("katarzyna", "hasłokatarzyna"));
        hotel.add(new Client("magda", "hasłomagda"));

        hotel.add(RoomType.SINGLE);
        hotel.add(RoomType.SINGLE);
        hotel.add(RoomType.DOUBLE);
        hotel.add(RoomType.DOUBLE);
        hotel.add(RoomType.APARTMENT);

        hotel.login("jan", "hasłojan");
        hotel.book(1);
        hotel.pay(1, PaymentMethod.CARD);
        hotel.arrive(1);
        hotel.leave(1);

        hotel.login("anna", "hasłoanna");
        hotel.book(2);
        hotel.cancel(2);

        hotel.login("piotr", "hasłopiotr");
        hotel.book(3);
        hotel.arrive(3);

        hotel.login("anna", "hasłoanna");
        hotel.book(4);
        hotel.pay(4, PaymentMethod.CASH);

        hotel.login("adam", "hasłoadam");
        hotel.clean(5);
        hotel.startRenovation(5);
        hotel.endRenovation(5);
    } catch (Exception e) {
        IO.println("Wystąpił błąd podczas tworzenia danych startowych: " + e.getMessage());
    }

    hotel.render(new WelcomeScreen());
}