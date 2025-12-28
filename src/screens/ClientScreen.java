package screens;

import exceptions.InvalidRoomNumberException;
import exceptions.InvalidStatusException;
import model.Room;

import java.util.List;

public class ClientScreen extends Screen {
    private List<Room> availableRooms;

    @Override
    public void render() {
        System.out.println("Panel gościa\n");
        System.out.println("Napisz 1, aby wyświetlić listę dostępnych pokoi.");
        System.out.println("Napisz 2, aby zarezerwować pokój.");
        switch (INPUT.nextInt()) {
            case 1 -> showAvailableRooms();
            case 2 -> {
                System.out.println("Podaj numer pokoju, który chcesz zarezerwować:");
                book(INPUT.nextInt());
            }
        }
    }

    private void showAvailableRooms() {
        availableRooms = hotel.getAvailableRooms();

        if (availableRooms.isEmpty()) {
            System.out.println("\nObecnie wszystkie pokoje są niedostępne. Spróbuj ponownie później.\n");
            return;
        }

        System.out.println("\nLista dostępnych pokoi:");
        System.out.println("Nr\t\t\t\tTyp");
        for (Room r : availableRooms) {
            System.out.println(r.getNumber() + "\t\t\t\t" + r.getType());
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
}
