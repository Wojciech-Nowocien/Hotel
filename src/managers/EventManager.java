package managers;

import events.AdminEvent;
import events.ClientRoomEvent;
import events.Event;
import events.Payment;
import events.enums.AdminEventType;
import events.enums.ClientEventType;
import events.enums.PaymentMethod;
import events.enums.availability.AvailabilityImpact;
import events.enums.availability.AvailabilityRequirement;
import events.enums.availability.Status;
import exceptions.InvalidStatusException;
import exceptions.RenovationException;
import exceptions.AlreadyCheckedInException;
import exceptions.RoomNotPaidException;
import model.Admin;
import model.Client;
import model.Room;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {
    private final ArrayList<Event> events;

    public EventManager() {
        events = new ArrayList<>();
    }

    private void add(Event event) throws InvalidStatusException {
        Status status = getLastStatus(event.getRoom());

        if (!event.canBeAppliedFor(status)) throw new InvalidStatusException(status, event.getRequirement());

        events.add(event);
    }

    private List<Event> getRoomRenovationEvents(Room room) {
        return events.stream().filter(e ->
                        e.getRoom().equals(room)
                                && e.isExecutorAdmin()
                                && e instanceof AdminEvent
                                && (((AdminEvent) e).getType() == AdminEventType.START_RENOVATION
                                || ((AdminEvent) e).getType() == AdminEventType.END_RENOVATION))
                .toList();
    }

    public boolean isRoomBookedBy(Room room, Client client) {
        var clientEvents = events.stream().filter(e -> e.getUser().equals(client)
                && e instanceof ClientRoomEvent
                && e.getRoom().equals(room)
        ).toList();

        if (clientEvents.isEmpty()) return false;

        return ((ClientRoomEvent) clientEvents.getLast()).getType() == ClientEventType.BOOK;
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
        return events.stream().filter(e -> e.getRoom().number() == number).toList();
    }

    public boolean isRoomOccupiedBy(Room room, Client client) {
        var roomEvents = events.stream().filter(e -> e.getRoom().equals(room) && e instanceof ClientRoomEvent).toList();
        if (roomEvents.isEmpty()) return false;
        var last = (ClientRoomEvent) roomEvents.getLast();
        var t = last.getType();
        return (t == ClientEventType.BOOK || t == ClientEventType.ARRIVE) && last.getUser().equals(client);
    }

    public boolean isRoomPaidBy(Room room, Client client) {
        var roomEvents = events.stream().filter(e -> e.getRoom().equals(room)).toList();
        if (roomEvents.isEmpty()) return true;

        int lastLeaveOrCancelIndex = -1;
        for (int i = 0; i < roomEvents.size(); i++) {
            Event e = roomEvents.get(i);
            if (e instanceof ClientRoomEvent event) {
                if (event.getType() == ClientEventType.LEAVE || event.getType() == ClientEventType.CANCEL) {
                    lastLeaveOrCancelIndex = i;
                }
            }
        }

        int sessionStartIndex = -1;
        for (int i = lastLeaveOrCancelIndex + 1; i < roomEvents.size(); i++) {
            Event e = roomEvents.get(i);
            if (e instanceof ClientRoomEvent event) {
                if (event.getType() == ClientEventType.BOOK || event.getType() == ClientEventType.ARRIVE) {
                    if (event.getUser().equals(client)) {
                        sessionStartIndex = i;
                        break;
                    } else {
                        return true;
                    }
                }
            }
        }

        if (sessionStartIndex == -1) return true;

        for (int i = sessionStartIndex; i < roomEvents.size(); i++) {
            Event e = roomEvents.get(i);
            if (e instanceof Payment && e.getUser().equals(client)) return true;
            if (e instanceof ClientRoomEvent event) {
                if (event.getType() == ClientEventType.LEAVE || event.getType() == ClientEventType.CANCEL) break;
                if (!event.getUser().equals(client) && (event.getType() == ClientEventType.BOOK || event.getType() == ClientEventType.ARRIVE)) break;
            }
        }

        return false;
    }

    public void clean(Room room, Admin admin) {
        AdminEvent clean = new AdminEvent(room, admin, AdminEventType.CLEAN,
                AvailabilityImpact.NONE, AvailabilityRequirement.NONE);

        try {
            add(clean);
        } catch (InvalidStatusException e) {
            // never happens because of AvailabilityRequirement.NONE
            throw new RuntimeException(e);
        }
    }

    public void startRenovation(Room room, Admin admin) throws InvalidStatusException, RenovationException {
        var roomRenovations = getRoomRenovationEvents(room);

        AdminEvent renovation = new AdminEvent(room, admin, AdminEventType.START_RENOVATION,
                AvailabilityImpact.UNAVAILABLE, AvailabilityRequirement.REQUIRE_AVAILABLE);

        if (roomRenovations.isEmpty()) {
            add(renovation);
            return;
        }
        if (((AdminEvent) roomRenovations.getLast()).getType() == AdminEventType.START_RENOVATION) {
            throw new RenovationException();
        }

        add(renovation);
    }

    public void endRenovation(Room room, Admin admin) throws RenovationException {
        var roomRenovations = getRoomRenovationEvents(room);

        AdminEvent renovation = new AdminEvent(room, admin, AdminEventType.END_RENOVATION,
                AvailabilityImpact.AVAILABLE, AvailabilityRequirement.REQUIRE_UNAVAILABLE);

        if (roomRenovations.isEmpty() || ((AdminEvent) roomRenovations.getLast()).getType() == AdminEventType.END_RENOVATION) {
            throw new RenovationException();
        }

        events.add(renovation);
    }

    public void book(Room room, Client client) throws InvalidStatusException {
        ClientRoomEvent book = new ClientRoomEvent(room, client, ClientEventType.BOOK, AvailabilityImpact.UNAVAILABLE,
                AvailabilityRequirement.REQUIRE_AVAILABLE);
        add(book);
    }

    public void cancel(Room room, Client client) throws InvalidStatusException {
        if (!isRoomBookedBy(room, client)) {
            throw new InvalidStatusException(getLastStatus(room), AvailabilityRequirement.REQUIRE_UNAVAILABLE);
        }
        ClientRoomEvent cancel = new ClientRoomEvent(room, client, ClientEventType.CANCEL, AvailabilityImpact.AVAILABLE,
                AvailabilityRequirement.REQUIRE_UNAVAILABLE);
        add(cancel);
    }

    public boolean isClientCheckedIn(Client client) {
        var allClientEvents = events.stream().filter(e -> e.getUser().equals(client)
                && e instanceof ClientRoomEvent).toList();

        if (allClientEvents.isEmpty()) return false;

        var roomsWithEvents = allClientEvents.stream()
                .map(Event::getRoom)
                .distinct()
                .toList();

        for (Room room : roomsWithEvents) {
            var lastRoomEvent = allClientEvents.stream()
                    .filter(e -> e.getRoom().equals(room))
                    .toList()
                    .getLast();

            if (lastRoomEvent instanceof ClientRoomEvent &&
                    ((ClientRoomEvent) lastRoomEvent).getType() == ClientEventType.ARRIVE) {
                return true;
            }
        }

        return false;
    }

    public void arrive(Room room, Client client) throws InvalidStatusException, AlreadyCheckedInException {
        if (!isRoomBookedBy(room, client))
            throw new InvalidStatusException(getLastStatus(room), AvailabilityRequirement.REQUIRE_UNAVAILABLE);

        if (isClientCheckedIn(client))
            throw new AlreadyCheckedInException();

        ClientRoomEvent arrive = new ClientRoomEvent(room, client, ClientEventType.ARRIVE, AvailabilityImpact.UNAVAILABLE,
                AvailabilityRequirement.REQUIRE_UNAVAILABLE);
        events.add(arrive);
    }

    public void pay(Room room, Client client, PaymentMethod method) throws InvalidStatusException {
        if (!isRoomOccupiedBy(room, client)) {
            throw new InvalidStatusException(getLastStatus(room), AvailabilityRequirement.REQUIRE_UNAVAILABLE);
        }
        Payment payment = new Payment(room, client, method);
        add(payment);
    }

    public void leave(Room room, Client client) throws InvalidStatusException, RoomNotPaidException {
        var roomEvents = events.stream().filter(e -> e.getRoom().equals(room)).toList();
        if (roomEvents.isEmpty()) {
            throw new InvalidStatusException(getLastStatus(room), AvailabilityRequirement.REQUIRE_AVAILABLE);
        }

        int lastClientIndex = -1;
        Event lastClientEvent = null;
        for (int i = 0; i < roomEvents.size(); i++) {
            Event e = roomEvents.get(i);
            if (e instanceof ClientRoomEvent) {
                lastClientIndex = i;
                lastClientEvent = e;
            }
        }

        if (lastClientIndex == -1) {
            throw new InvalidStatusException(getLastStatus(room), AvailabilityRequirement.REQUIRE_AVAILABLE);
        }

        ClientRoomEvent cre = (ClientRoomEvent) lastClientEvent;
        if (cre.getType() != ClientEventType.ARRIVE || !cre.getUser().equals(client)) {
            throw new InvalidStatusException(getLastStatus(room), AvailabilityRequirement.REQUIRE_UNAVAILABLE);
        }

        if (!isRoomPaidBy(room, client)) {
            throw new RoomNotPaidException();
        }

        ClientRoomEvent leave = new ClientRoomEvent(room, client, ClientEventType.LEAVE, AvailabilityImpact.AVAILABLE,
                AvailabilityRequirement.REQUIRE_UNAVAILABLE);
        add(leave);
    }

    public Room getClientCurrentRoom(Client client) {
        Map<Room, ClientRoomEvent> lastClientEventByRoom = new HashMap<>();

        for (Event e : events) {
            if (e instanceof ClientRoomEvent) {
                lastClientEventByRoom.put(e.getRoom(), (ClientRoomEvent) e);
            }
        }

        for (Map.Entry<Room, ClientRoomEvent> entry : lastClientEventByRoom.entrySet()) {
            ClientRoomEvent cre = entry.getValue();
            if (cre.getType() == ClientEventType.ARRIVE && cre.getUser().equals(client)) {
                return entry.getKey();
            }
        }

        return null;
    }
}
