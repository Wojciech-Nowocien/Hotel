package events.enums;

public enum PaymentMethod {
    CASH,
    CARD,
    BLIK,
    TRANSFER;

    @Override
    public String toString() {
        return switch (this) {
            case CASH -> "gotówka";
            case CARD -> "karta";
            case BLIK -> "blik";
            case TRANSFER -> "przelew";
        };
    }
}
