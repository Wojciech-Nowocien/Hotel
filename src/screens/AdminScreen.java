package screens;

import managers.RoomManager;
import model.Room;
import model.RoomType;

public class AdminScreen extends Screen {
    private final RoomManager rooms;

    public AdminScreen() {
        this.rooms = hotel.getRooms();
    }

    @Override
    public void render() {
        System.out.println("Panel administratora\n");
        System.out.println("Napisz 1, aby dodać nowy pokój.");
        switch (INPUT.nextInt()) {
            case 1 -> addRoom();
        }
    }

    private void addRoom() {
        System.out.println("Dodaj pokój\n");
        System.out.println("Dostępne rodzaje pokojów to: single, double, apartment");
        System.out.println("Podaj rodzaj pokoju (wielkość liter nie ma znaczenia):");
        try {
            RoomType type = RoomType.valueOf(INPUT.nextLine().toUpperCase());
            Room r = rooms.add(type);
            System.out.println("Nowy pokój typu " + r.getType() + " jest dostępny pod numerem " + r.getNumber() + ".\n");
        } catch (IllegalArgumentException e) {
            System.out.println("\nBłąd: Niepoprawny rodzaj pokoju!\n");
        }
    }
}
