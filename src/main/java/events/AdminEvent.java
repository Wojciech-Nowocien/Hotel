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
    AvailabilityImpact getStatus() {
        return impact;
    }

    @Override
    AvailabilityRequirement getRequirement() {
        return requirement;
    }
}
