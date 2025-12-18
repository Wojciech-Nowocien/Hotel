package events;

import model.Admin;
import model.Room;

public class AdminEvent extends Event{
    public AdminEvent(Room room, Admin admin) {
        super(room, admin);
    }
}
