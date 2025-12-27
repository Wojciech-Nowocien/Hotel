package screens;

import model.Room;
import model.RoomType;

public class AdminScreen extends Screen {
    @Override
    public void render() {
        System.out.println("Panel administratora\n");
        System.out.println("Napisz 1, aby dodać nowy pokój.");
        System.out.println("Napisz 2, aby wyświetlić wszystkie pokoje.");
        switch (INPUT.nextInt()) {
            case 1 -> addRoom();
            case 2 -> showAllRooms();
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
}
