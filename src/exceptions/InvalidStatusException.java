package exceptions;

import events.enums.availability.AvailabilityRequirement;
import events.enums.availability.Status;

public class InvalidStatusException extends Exception {
    private final Status current;
    private final Status expected;

    public InvalidStatusException(Status current, AvailabilityRequirement requirement) {
        this.current = current;
        if (requirement == AvailabilityRequirement.REQUIRE_AVAILABLE) {
            this.expected = Status.AVAILABLE;
        } else {
            this.expected = Status.UNAVAILABLE;
        }
    }

    public Status getCurrent() {
        return current;
    }

    public Status getExpected() {
        return expected;
    }
}
