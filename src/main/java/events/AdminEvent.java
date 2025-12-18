package events;

import events.enums.AdminEventType;
import model.Admin;
import model.Room;

public class AdminEvent extends Event {
    private final AdminEventType type;

    public AdminEvent(Room room, Admin admin, AdminEventType type) {
        super(room, admin);
        this.type = type;
    }

    public AdminEventType getType() {
        return type;
    }
}
