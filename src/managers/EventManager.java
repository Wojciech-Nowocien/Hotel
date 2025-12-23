package managers;

import events.Event;
import events.enums.availability.AvailabilityImpact;
import events.enums.availability.Status;
import model.Room;

import java.util.ArrayList;

public class EventManager {
    private final ArrayList<Event> events;

    public EventManager() {
        events = new ArrayList<>();
    }

    public boolean add(Event event) {
        Status status = getLastStatus(event.getRoom());

        if (!event.canBeAppliedFor(status)) return false;

        return events.add(event);
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
