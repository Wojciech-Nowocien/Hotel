package screens;

import exceptions.InvalidRoomNumberException;
import exceptions.InvalidStatusException;
import exceptions.AlreadyCheckedInException;
import model.Room;

import java.util.List;

public class ClientScreen extends Screen {
    private List<Room> rooms;

    @Override
    public void render() {
        System.out.println("Panel gościa\n");
        System.out.println("Napisz 1, aby wyświetlić listę dostępnych pokoi.");
        System.out.println("Napisz 2, aby zarezerwować pokój.");
        System.out.println("Napisz 3, aby zobaczyć zarezerwowane pokoje.");
        System.out.println("Napisz 4, aby anulować rezerwację pokoju.");
        System.out.println("Napisz 5, aby zameldować się w zarezerwowanym pokoju.");
        switch (INPUT.nextInt()) {
            case 1 -> showAvailableRooms();
            case 2 -> {
                System.out.println("Podaj numer pokoju, który chcesz zarezerwować:");
                book(INPUT.nextInt());
            }
            case 3 -> showBookedRooms();
            case 4 -> {
                System.out.println("Podaj numer pokoju, którego rezerwację chcesz anulować:");
                cancel(INPUT.nextInt());
            }
            case 5 -> {
                System.out.println("Podaj numer pokoju, w którym chcesz się zameldować:");
                arrive(INPUT.nextInt());
            }
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
}
