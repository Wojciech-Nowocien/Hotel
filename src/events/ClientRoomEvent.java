package events;

import events.enums.ClientEventType;
import events.enums.availability.AvailabilityImpact;
import events.enums.availability.AvailabilityRequirement;
import model.Client;
import model.Room;

public class ClientRoomEvent extends Event{
    private final ClientEventType type;
    private final AvailabilityImpact impact;
    private final AvailabilityRequirement requirement;

    public ClientRoomEvent(Room room, Client client, ClientEventType type, AvailabilityImpact impact,
                           AvailabilityRequirement requirement) {
        super(room, client);
        this.type = type;
        this.impact = impact;
        this.requirement = requirement;
    }

    public ClientEventType getType() {
        return type;
    }

    @Override
    public AvailabilityImpact getStatusImpact() {
        return impact;
    }

    @Override
    protected AvailabilityRequirement getRequirement() {
        return requirement;
    }

    @Override
    public String getMessage() {
        return switch (type){
            case BOOK -> "zarezerwowano";
            case ARRIVE -> "zameldowano";
            case LEAVE -> "wymeldowano";
            case CANCEL -> "anulowano rezerwację";
        };
    }
}
