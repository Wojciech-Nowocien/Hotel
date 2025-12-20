package events;

import events.enums.availability.AvailabilityImpact;
import events.enums.availability.Status;
import model.Room;

import java.util.ArrayList;

public class EventManager {
    private final ArrayList<Event> events;

    public EventManager() {
        events = new ArrayList<>();
    }

    private Status getLastStatus(Room room) {
        Event event = events.stream().filter(e ->
                e.getStatusImpact() != AvailabilityImpact.NONE && e.getRoom().equals(room)
        ).toList().getLast();

        if (event.getStatusImpact() == AvailabilityImpact.AVAILABLE) {
            return Status.AVAILABLE;
        } else {
            return Status.UNAVAILABLE;
        }
    }
}
