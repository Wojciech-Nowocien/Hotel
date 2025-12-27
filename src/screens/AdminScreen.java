package screens;

import events.Event;
import exceptions.InvalidRoomNumberException;
import model.Room;
import model.RoomType;

import java.util.List;

public class AdminScreen extends Screen {
    private int number;
    private List<Event> events;

    @Override
    public void render() {
        System.out.println("Panel administratora\n");
        System.out.println("Napisz 1, aby dodać nowy pokój.");
        System.out.println("Napisz 2, aby wyświetlić wszystkie pokoje.");
        System.out.println("Napisz 3, aby wyświetlić historię danego pokoju.");
        switch (INPUT.nextInt()) {
            case 1 -> addRoom();
            case 2 -> showAllRooms();
            case 3 -> showRoomHistory();
        }
    }

    private void addRoom() {
        System.out.println("Dodaj pokój\n");
        System.out.println("Dostępne rodzaje pokojów to: single, double, apartment");
        System.out.println("Podaj rodzaj pokoju (wielkość liter nie ma znaczenia):");
        try {
            RoomType type = RoomType.valueOf(INPUT.nextLine().toUpperCase());
            Room r = hotel.add(type);
            System.out.println("Nowy pokój typu " + r.getType() + " jest dostępny pod numerem " + r.getNumber() + ".\n");
        } catch (IllegalArgumentException e) {
            System.out.println("\nBłąd: Niepoprawny rodzaj pokoju!\n");
        }
    }

    private void showAllRooms() {
        if (hotel.getRooms().isEmpty()) {
            System.out.println("Obecnie hotel nie ma żadnych pokoi!\n");
            return;
        }

        System.out.println("Lista pokoi:");

        System.out.println("Nr\t\t\t\tTyp\t\t\t\t\tStatus");
        for (Room r : hotel.getRooms()) {
            System.out.println(r.getNumber() + "\t\t\t\t" + r.getType() + "\t\t\t" + hotel.getLastStatus(r));
        }
        System.out.println();
    }

    private void showRoomHistory() {
        System.out.println("Podaj numer pokoju:");
        number = INPUT.nextInt();
        try {
            events = hotel.getEventsByRoomNumber(number);
            if (!events.isEmpty()) {
                System.out.println("Nazwa\t\t\tWykonawca\t\tRola wyk.\t\t\tNr. pokoju\t\t\tTyp pokoju\t\t\tDodatkowe informacje");
                for (Event event : events) {
                    System.out.println(event);
                }
            } else {
                System.out.println("Historia tego pokoju jest pusta.");
            }
        } catch (InvalidRoomNumberException e) {
            System.out.println("\nBłąd: Pokój o wskazanym numerze nie istnieje!");
            System.out.println("Aby zobaczyć istniejące pokoje wybierz opcję 2 w panelu administratora.\n");
        }
    }
}
