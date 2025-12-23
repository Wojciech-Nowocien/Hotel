package model;

import managers.EventManager;
import managers.UserManager;
import screens.Screen;

import java.util.ArrayList;

public class Hotel {
    public static final Hotel HOTEL = new Hotel();

    private final EventManager events;
    private final ArrayList<Room> rooms;
    private final UserManager users;
    private Screen screen;

    public Hotel() {
        events = new EventManager();
        rooms = new ArrayList<>();
        users = new UserManager();
        this.screen = null;
    }

    public EventManager getEvents() {
        return events;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public UserManager getUsers() {
        return users;
    }

    public Screen getScreen() {
        return screen;
    }

    public void setScreen(Screen screen) {
        if (screen != null) {
            this.screen = screen;
        }
    }

    public void render(Screen screen) {
        this.screen = screen;
        while (true) {
            this.screen.render();
        }
    }
}

