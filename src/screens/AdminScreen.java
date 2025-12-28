package screens;

import events.Event;
import exceptions.InvalidRoomNumberException;
import exceptions.InvalidStatusException;
import exceptions.RenovationException;
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
        System.out.println("Napisz 4, aby zarządzać pokojem.");
        System.out.println("Napisz 6, aby się wylogować.");
        switch (INPUT.nextInt()) {
            case 1 -> addRoom();
            case 2 -> showAllRooms();
            case 3 -> showRoomHistory();
            case 4 -> manageRoom();
            case 6 -> {
                System.out.println("\nDziękujemy za korzystanie z panelu administratora. Do zobaczenia!\n");
                changeScreen(new WelcomeScreen());
            }
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

    private void handleInvalidRoomNumberException() {
        System.out.println("\nBłąd: Pokój o wskazanym numerze nie istnieje!");
        System.out.println("Aby zobaczyć istniejące pokoje wybierz opcję 2 w panelu administratora.\n");
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
            handleInvalidRoomNumberException();
        }
    }

    private void manageRoom() {
        boolean shouldContinue = true;

        while (shouldContinue) {
            System.out.println("Zarządzanie pokojami");
            System.out.println("Napisz 1, żeby powrócić do panelu administratora");
            System.out.println("Napisz 2, żeby posprzątać pokój");
            System.out.println("Napisz 3, żeby rozpocząć remont pokoju.");
            System.out.println("Napisz 4, żeby zakończyć remont w pokoju.");
            switch (INPUT.nextInt()) {
                case 1 -> shouldContinue = false;
                case 2 -> {
                    System.out.println("Podaj numer pokoju, który chcesz posprzątać:");
                    clean(INPUT.nextInt());
                }
                case 3 -> {
                    System.out.println("Podaj numer pokoju, który chcesz wyremontować:");
                    startRenovation(INPUT.nextInt());
                }
                case 4 ->{
                    System.out.println("Podaj numer pokoju, w którym remont ma się zakończyć:");
                    endRenovation(INPUT.nextInt());
                }
            }
        }
    }

    private void clean(int number) {
        try {
            hotel.clean(number);
            System.out.println("\nPomyślnie posprzątano pokój numer " + number + ".\n");
        } catch (InvalidRoomNumberException e) {
            handleInvalidRoomNumberException();
        }
    }

    private void startRenovation(int number) {
        try {
            hotel.startRenovation(number);
            System.out.println("\nPomyślnie rozpoczęto remont w pokoju numer " + number + ".\n");
        } catch (InvalidRoomNumberException e) {
            handleInvalidRoomNumberException();
        } catch (RenovationException e) {
            System.out.println("\nBłąd: we wskazanym pokoju remont już trwa!");
            System.out.println("Aby rozpocząć kolejny musisz zakończyć istniejący.\n");
        } catch (InvalidStatusException e) {
            System.out.println("\nBłąd: wskazany pokój jest niedostępny!");
            System.out.println("Bardzo możliwe, że ktoś w nim jest lub go zarezerwował.");
            System.out.println("Zobacz historię tego pokoju (opcja nr. 3 w panelu administratora), aby poznać szczegóły\n");
        }
    }

    private void endRenovation(int number) {
        try {
            hotel.endRenovation(number);
            System.out.println("\nPomyślnie zakończono remont w pokoju numer " + number + ".\n");
        } catch (InvalidRoomNumberException e) {
            handleInvalidRoomNumberException();
        } catch (RenovationException e) {
            System.out.println("\nBłąd: we wskazanym pokoju nie ma żadnego remontu!");
            System.out.println("Aby zakończyć remont musisz go najpierw rozpocząć.\n");
        }
    }
}
