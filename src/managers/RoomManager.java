package managers;

import model.Room;
import model.RoomType;

import java.util.ArrayList;

public class RoomManager {
    private final ArrayList<Room> rooms;

    public RoomManager() {
        this.rooms = new ArrayList<>();
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public void add(RoomType type) {
        int number = 1;

        if (!rooms.isEmpty()) {
            number = rooms.getLast().getNumber() + 1;
        }

        rooms.add(new Room(number, type));
    }
}
