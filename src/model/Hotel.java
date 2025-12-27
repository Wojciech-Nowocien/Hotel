package model;

import events.enums.availability.Status;
import exceptions.DuplicateLoginException;
import exceptions.InvalidPasswordException;
import exceptions.LoginNotFoundException;
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

    public Status getLastStatus(Room room){
        return events.getLastStatus(room);
    }

    public void render(Screen screen) {
        this.screen = screen;
        while (true) {
            this.screen.render();
        }
    }
}

