package model;

import events.Event;
import events.enums.availability.Status;
import events.enums.PaymentMethod;
import exceptions.*;
import managers.EventManager;
import managers.RoomManager;
import managers.UserManager;
import screens.Screen;

import java.util.List;

public class Hotel {
    public static final Hotel HOTEL = new Hotel();

    private final EventManager events;
    private final RoomManager rooms;
    private final UserManager users;
    private Screen screen;

    public Hotel() {
        events = new EventManager();
        rooms = new RoomManager();
        users = new UserManager();
        this.screen = null;
    }

    public List<Room> getRooms() {
        return rooms.getRooms();
    }

    public List<Room> getAvailableRooms() {
        var allRooms = rooms.getRooms();
        return allRooms.stream().filter(r -> events.getLastStatus(r) == Status.AVAILABLE).toList();
    }

    public List<Room> getBookedRooms() {
        var allRooms = rooms.getRooms();
        return allRooms.stream().filter(r -> events.isRoomBookedBy(r, new Client(getCurrentUser()))).toList();
    }

    public List<Room> getUnpaidRooms() {
        var allRooms = rooms.getRooms();
        Client client = new Client(getCurrentUser());
        return allRooms.stream().filter(r -> events.isRoomOccupiedBy(r, client) && !events.isRoomPaidBy(r, client)).toList();
    }

    public void setScreen(Screen screen) {
        if (screen != null) {
            this.screen = screen;
        }
    }

    public void add(User user) throws DuplicateLoginException {
        users.add(user);
    }

    public Room add(RoomType type) {
        return rooms.add(type);
    }

    public User getCurrentUser() {
        return users.getCurrentUser();
    }

    public void login(String login, String password) throws LoginNotFoundException, InvalidPasswordException {
        users.login(login, password);
    }

    public Status getLastStatus(Room room) {
        return events.getLastStatus(room);
    }

    public List<Event> getEventsByRoomNumber(int number) throws InvalidRoomNumberException {
        rooms.validate(number);
        return events.getEventsByRoomNumber(number);
    }

    public void clean(int number) throws InvalidRoomNumberException {
        Room room = rooms.getRoomByNumber(number);
        events.clean(room, new Admin(getCurrentUser()));
    }

    public void startRenovation(int number) throws InvalidRoomNumberException, InvalidStatusException,
            RenovationException {
        Room room = rooms.getRoomByNumber(number);
        events.startRenovation(room, new Admin(getCurrentUser()));
    }

    public void endRenovation(int number) throws InvalidRoomNumberException, RenovationException {
        Room room = rooms.getRoomByNumber(number);
        events.endRenovation(room, new Admin(getCurrentUser()));
    }

    public void book(int number) throws InvalidRoomNumberException, InvalidStatusException {
        Room room = rooms.getRoomByNumber(number);
        events.book(room, new Client(getCurrentUser()));
    }

    public void cancel(int number) throws InvalidRoomNumberException, InvalidStatusException {
        Room room = rooms.getRoomByNumber(number);
        events.cancel(room, new Client(getCurrentUser()));
    }

    public void arrive(int number) throws InvalidRoomNumberException, InvalidStatusException, AlreadyCheckedInException {
        Room room = rooms.getRoomByNumber(number);
        events.arrive(room, new Client(getCurrentUser()));
    }

    public void pay(int number, PaymentMethod method) throws InvalidRoomNumberException, InvalidStatusException {
        Room room = rooms.getRoomByNumber(number);
        events.pay(room, new Client(getCurrentUser()), method);
    }

    public void leave(int number) throws InvalidRoomNumberException, InvalidStatusException, RoomNotPaidException {
        Room room = rooms.getRoomByNumber(number);
        events.leave(room, new Client(getCurrentUser()));
    }

    public void render(Screen screen) {
        this.screen = screen;
        while (true) {
            this.screen.render();
        }
    }
}
