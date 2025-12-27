package events.enums.availability;

public enum Status {
    AVAILABLE,
    UNAVAILABLE;

    @Override
    public String toString() {
        if (this == AVAILABLE) {
            return "dostępny";
        } else {
            return "niedostępny";
        }
    }
}
