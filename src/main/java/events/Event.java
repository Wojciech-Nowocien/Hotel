package events;

import events.enums.availability.AvailabilityImpact;
import events.enums.availability.AvailabilityRequirement;
import model.Admin;
import model.Client;
import model.Room;
import model.User;

public abstract class Event {
    private final Room room;
    private final User user;

    public Event(Room room, User user) {
        this.room = room;
        this.user = user;
    }

    public Room getRoom() {
        return room;
    }

    public User getUser() {
        return user;
    }

    public boolean isExecutorClient() {
        return user instanceof Client;
    }

    public boolean isExecutorAdmin() {
        return user instanceof Admin;
    }

    abstract AvailabilityImpact getStatus();

    abstract AvailabilityRequirement getRequirement();
}
