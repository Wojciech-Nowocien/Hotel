package events;

import events.enums.AdminEventType;
import events.enums.availability.AvailabilityImpact;
import events.enums.availability.AvailabilityRequirement;
import model.Admin;
import model.Room;

public class AdminEvent extends Event {
    private final AdminEventType type;
    private final AvailabilityImpact impact;
    private final AvailabilityRequirement requirement;

    public AdminEvent(Room room, Admin admin, AdminEventType type, AvailabilityImpact impact,
                      AvailabilityRequirement requirement) {
        super(room, admin);
        this.type = type;
        this.impact = impact;
        this.requirement = requirement;
    }

    public AdminEventType getType() {
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
            case CLEAN -> "posprzątano";
            case START_RENOVATION -> "rozpoczęto remont";
            case END_RENOVATION -> "zakończono remont";
        };
    }
}
