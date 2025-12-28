package events;

import events.enums.availability.AvailabilityImpact;
import events.enums.availability.AvailabilityRequirement;
import events.enums.availability.Status;
import model.Admin;
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

    public boolean isExecutorAdmin() {
        return user instanceof Admin;
    }

    public boolean canBeAppliedFor(Status status) {
        return switch (getRequirement()) {
            case NONE -> true;
            case REQUIRE_AVAILABLE -> status == Status.AVAILABLE;
            case REQUIRE_UNAVAILABLE -> status == Status.UNAVAILABLE;
        };
    }

    public abstract AvailabilityImpact getStatusImpact();

    public abstract AvailabilityRequirement getRequirement();

    public abstract String getMessage();

    @Override
    public String toString() {

        String name = String.format("%-26s", getMessage());
        String executor = String.format("%-18s", getUser().toString());
        String role = isExecutorAdmin() ? String.format("%-15s", "administrator") : String.format("%-15s", "gość");
        String roomNumber = String.format("%-16s", room.number());
        String roomType = String.format("%-16s", room.type());
        String info = "";

        return name + executor + role + roomNumber + roomType + info;
    }
}
