package events.enums.availability;

public enum AvailabilityRequirement {
    NONE,
    REQUIRE_AVAILABLE,
    REQUIRE_UNAVAILABLE;

    public boolean isCompatibleWith(Status status) {
        return switch (this) {
            case NONE -> true;
            case REQUIRE_AVAILABLE -> status == Status.AVAILABLE;
            case REQUIRE_UNAVAILABLE -> status == Status.UNAVAILABLE;
        };
    }
}
