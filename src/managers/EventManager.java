package managers;

import events.Event;
import events.enums.availability.AvailabilityImpact;
import events.enums.availability.Status;
import exceptions.InvalidStatusException;
import model.Room;

import java.util.ArrayList;
import java.util.List;

public class EventManager {
    private final ArrayList<Event> events;

    public EventManager() {
        events = new ArrayList<>();
    }

    public void add(Event event) throws InvalidStatusException{
        Status status = getLastStatus(event.getRoom());

        if (!event.canBeAppliedFor(status)) throw new InvalidStatusException(status, event.getRequirement());

        events.add(event);
    }

    public Status getLastStatus(Room room) {
        var roomEvents = events.stream().filter(e ->
                e.getStatusImpact() != AvailabilityImpact.NONE && e.getRoom().equals(room)
        ).toList();

        if (roomEvents.isEmpty()) {
            return Status.AVAILABLE;
        }

        if (roomEvents.getLast().getStatusImpact() == AvailabilityImpact.AVAILABLE) {
            return Status.AVAILABLE;
        } else {
            return Status.UNAVAILABLE;
        }
    }

    public List<Event> getEventsByRoomNumber(int number) {
        return events.stream().filter(e -> e.getRoom().getNumber() == number).toList();
    }
}
