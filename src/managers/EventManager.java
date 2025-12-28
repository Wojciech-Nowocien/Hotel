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
import model.Admin;
import model.Client;
import model.Room;

import java.util.ArrayList;
import java.util.List;

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

    private List<ClientRoomEvent> getClientRoomEvents(Client client, Room room) {
        List<ClientRoomEvent> result = new ArrayList<>();

        events.stream().filter(e -> e.getUser().equals(client)
                        && e instanceof ClientRoomEvent
                        && e.getRoom().equals(room)
                ).
                forEach(e -> result.add((ClientRoomEvent) e));

        return result;
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
        return events.stream().filter(e -> e.getRoom().getNumber() == number).toList();
    }

    public boolean isRoomOccupiedBy(Room room, Client client) {
        var roomEvents = events.stream().filter(e -> e.getRoom().equals(room) && e instanceof ClientRoomEvent).toList();
        if (roomEvents.isEmpty()) return false;
        var last = (ClientRoomEvent) roomEvents.getLast();
        var t = last.getType();
        if ((t == ClientEventType.BOOK || t == ClientEventType.ARRIVE) && last.getUser().equals(client)) {
            return true;
        }
        return false;
    }

    public boolean isRoomPaidBy(Room room, Client client) {
        var roomEvents = events.stream().filter(e -> e.getRoom().equals(room)).toList();
        if (roomEvents.isEmpty()) return true;

        int lastClientIndex = -1;
        Object lastClient = null;
        ClientEventType lastClientType = null;
        int lastPaymentIndex = -1;
        Object lastPaymentClient = null;

        for (int i = 0; i < roomEvents.size(); i++) {
            Event e = roomEvents.get(i);
            if (e instanceof ClientRoomEvent) {
                lastClientIndex = i;
                lastClient = e.getUser();
                lastClientType = ((ClientRoomEvent) e).getType();
            } else if (e instanceof Payment) {
                lastPaymentIndex = i;
                lastPaymentClient = e.getUser();
            }
        }

        if (lastClientIndex == -1) return true;

        if (!(lastClient instanceof Client)) return true;

        if (!lastClient.equals(client)) return true;

        if (lastClientType == ClientEventType.BOOK || lastClientType == ClientEventType.ARRIVE) {
            if (lastPaymentIndex > lastClientIndex && lastPaymentClient != null && lastPaymentClient.equals(lastClient)) return true;
            return false;
        }

        return true;
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
}
