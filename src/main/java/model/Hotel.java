package model;

import managers.EventManager;
import managers.UserManager;
import screens.Screen;

import java.util.ArrayList;

public class Hotel {
    private final EventManager events;
    private final ArrayList<Room> rooms;
    private final UserManager users;
    private Screen screen;

    public Hotel(Screen screen) {
        events = new EventManager();
        rooms = new ArrayList<>();
        users = new UserManager();
        this.screen = screen;
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
        this.screen = screen;
    }

    public void render() {
        while (true) {
            screen.render();
        }
    }
}
