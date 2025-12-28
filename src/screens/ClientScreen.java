package screens;

import exceptions.InvalidRoomNumberException;
import exceptions.InvalidStatusException;
import exceptions.AlreadyCheckedInException;
import exceptions.RoomNotPaidException;
import events.enums.PaymentMethod;
import model.Room;

import java.util.List;

public class ClientScreen extends Screen {
    private List<Room> rooms;
    private Room currentRoom;

    @Override
    public void render() {
        currentRoom = hotel.getCurrentRoom();

        System.out.println("Panel gościa\n");
        if (currentRoom != null) {
            System.out.println("Obecnie jesteś zameldowany w pokoju numer " + currentRoom.getNumber() + ".");
        }
        System.out.println("Napisz 1, aby wyświetlić listę dostępnych pokoi.");
        System.out.println("Napisz 2, aby zarezerwować pokój.");
        System.out.println("Napisz 3, aby zobaczyć zarezerwowane pokoje.");
        System.out.println("Napisz 4, aby anulować rezerwację pokoju.");
        System.out.println("Napisz 5, aby zameldować się w zarezerwowanym pokoju.");
        System.out.println("Napisz 6, aby zobaczyć listę nieopłaconych pokoi.");
        System.out.println("Napisz 7, aby zapłacić za pokój.");
        System.out.println("Napisz 8, aby się wylogować.");
        if (currentRoom != null) {
            System.out.println("Napisz 9, aby wymeldować się z pokoju.");
        }
        switch (INPUT.nextInt()) {
            case 1 -> showAvailableRooms();
            case 2 -> {
                System.out.println("\nPodaj numer pokoju, który chcesz zarezerwować:");
                book(INPUT.nextInt());
            }
            case 3 -> showBookedRooms();
            case 4 -> {
                System.out.println("\nPodaj numer pokoju, którego rezerwację chcesz anulować:");
                cancel(INPUT.nextInt());
            }
            case 5 -> {
                System.out.println("\nPodaj numer pokoju, w którym chcesz się zameldować:");
                arrive(INPUT.nextInt());
            }
            case 6 -> showUnpaidRooms();
            case 7 -> {
                System.out.println("\nPodaj numer pokoju, za który chcesz zapłacić:");
                int number = INPUT.nextInt();
                System.out.println("Wybierz metodę płatności: CASH, CARD, BLIK, TRANSFER");
                try {
                    PaymentMethod method = PaymentMethod.valueOf(INPUT.nextLine().toUpperCase());
                    pay(number, method);
                } catch (IllegalArgumentException e) {
                    System.out.println("\nBłąd: Niepoprawna metoda płatności!");
                    System.out.println("Musisz wybrać opcję spośród dostępnych.\n");
                }
            }
            case 8 -> {
                System.out.println("\nDziękujemy za korzystanie z usług Hotelu. Zapraszamy ponownie!\n");
                changeScreen(new WelcomeScreen());
            }
            case 9 -> {
                if (currentRoom != null) {
                    leave(currentRoom.getNumber());
                } else {
                    System.out.println("\nBłąd: Nie możesz się wymeldować, ponieważ nie jesteś zameldowany w żadnym pokoju!\n");
                }
            }
            default -> System.out.println();
        }
    }

    private void showAvailableRooms() {
        rooms = hotel.getAvailableRooms();

        if (rooms.isEmpty()) {
            System.out.println("\nObecnie wszystkie pokoje są niedostępne. Spróbuj ponownie później.\n");
            return;
        }

        System.out.println("\nLista dostępnych pokoi:");
        System.out.println("Nr\t\t\t\t\tTyp");
        for (Room r : rooms) {
            System.out.println(r.getNumber() + "\t\t\t\t\t" + r.getType());
        }
        System.out.println();
    }

    private void book(int number) {
        try {
            hotel.book(number);
            System.out.println("\nPomyślnie zarezerwowano pokój numer " + number + ".\n");
        } catch (InvalidRoomNumberException e) {
            System.out.println("\nBłąd: Pokój o wskazanym numerze nie istnieje!");
            System.out.println("Przyjżyj się uważnie liście dostępnych pokoi.\n");
        } catch (InvalidStatusException e) {
            System.out.println("\nBłąd: Pokój o wskazanym numerze jest obecnie niedostępny!");
            System.out.println("Przyjżyj się uważnie liście dostępnych pokoi.\n");
        }
    }

    private void showBookedRooms() {
        rooms = hotel.getBookedRooms();

        if (rooms.isEmpty()) {
            System.out.println("\nObecnie nie masz zarezerwowanych żadnych pokoi.\n");
            return;
        }

        System.out.println("\nLista zarezerwowanych przez Ciebie pokoi:");
        System.out.println("Nr\t\t\t\t\tTyp");
        for (Room r : rooms) {
            System.out.println(r.getNumber() + "\t\t\t\t\t" + r.getType());
        }
        System.out.println();
    }

    private void cancel(int number) {
        try {
            hotel.cancel(number);
            System.out.println("\nPomyślnie anulowano rezerwację pokoju numer " + number + ".\n");
        } catch (InvalidRoomNumberException e) {
            System.out.println("\nBłąd: Pokój o wskazanym numerze nie istnieje!");
            System.out.println("Przyjżyj się uważnie liście zarezerwowanych pokoi.\n");
        } catch (InvalidStatusException e) {
            System.out.println("\nBłąd: Nie możesz anulować tej rezerwacji bo nie zarezerwowałeś tego pokoju!");
            System.out.println("Sprawdź listę swoich rezerwacji (opcja 3 w panelu gościa).\n");
        }
    }

    private void showUnpaidRooms() {
        rooms = hotel.getUnpaidRooms();

        if (rooms.isEmpty()) {
            System.out.println("\nNie masz żadnych należności wobec Hotelu.\n");
            return;
        }

        System.out.println("\nLista pokoi, które musisz opłacić:");
        System.out.println("Nr\t\t\t\t\tTyp");
        for (Room r : rooms) {
            System.out.println(r.getNumber() + "\t\t\t\t\t" + r.getType());
        }
        System.out.println();
    }

    private void arrive(int number) {
        try {
            hotel.arrive(number);
            System.out.println("\nPomyślnie zameldowano w pokoju numer " + number + ".\n");
        } catch (InvalidRoomNumberException e) {
            System.out.println("\nBłąd: Pokój o wskazanym numerze nie istnieje!");
            System.out.println("Sprawdź listę swoich rezerwacji (opcja 3 w panelu gościa).\n");
        } catch (AlreadyCheckedInException e) {
            System.out.println("\nBłąd: Jesteś już zameldowany w innym pokoju!");
            System.out.println("Aby zameldować się w tym pokoju, musisz się najpierw wymeldować z poprzedniego.\n");
        } catch (InvalidStatusException e) {
            System.out.println("\nBłąd: Nie możesz się zameldować w tym pokoju, którego nie zarezerwowałeś!");
            System.out.println("Sprawdź listę swoich rezerwacji (opcja 3 w panelu gościa).\n");
        }
    }

    private void pay(int number, PaymentMethod method) {
        try {
            hotel.pay(number, method);
            System.out.println("\nPłatność za pokój numer " + number + " zakończyła się pomyślnie.\n");
        } catch (InvalidRoomNumberException e) {
            System.out.println("\nBłąd: Pokój o wskazanym numerze nie istnieje!");
            System.out.println("Sprawdź listę swoich nieopłaconych pokojów.\n");
        } catch (InvalidStatusException e) {
            System.out.println("\nBłąd: Nie możesz zapłacić za ten pokój.");
            System.out.println("Jesteś pewien, że go wynajmujesz lub w nim przebywasz?");
            System.out.println("Może opłaciłeś go już wcześniej?");
            System.out.println("Zobacz listę nieopłaconych poki, aby dowiedzieć się więcej (opcja nr. 6 w panelu gościa).\n");
        }
    }

    private void leave(int number) {
        try {
            hotel.leave(number);
            System.out.println("\nPomyślnie wymeldowano z pokoju numer " + number + ".\n");
        } catch (InvalidRoomNumberException e) {
            System.out.println("\nBłąd: Pokój o wskazanym numerze nie istnieje!");
            System.out.println("Sprawdź listę swoich rezerwacji.\n");
        } catch (RoomNotPaidException e) {
            System.out.println("\nBłąd: Nie możesz się wymeldować przed opłaceniem pokoju!");
            System.out.println("Najpierw opłać pokój (opcja nr. 7 w panelu gościa), a dopiero potem się wymelduj.\n");
        } catch (InvalidStatusException e) {
            System.out.println("\nBłąd: Nie możesz opuścić pokoju, w którym Cię nie ma!");
            System.out.println("Wybierz właściwy pokój.\n");
        }
    }
}
