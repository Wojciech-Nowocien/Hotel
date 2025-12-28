package screens;

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
}
