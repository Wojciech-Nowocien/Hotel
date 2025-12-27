package model;

import events.Event;
import events.enums.availability.Status;
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

    public void render(Screen screen) {
        this.screen = screen;
        while (true) {
            this.screen.render();
        }
    }
}

