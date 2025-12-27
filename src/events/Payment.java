package events;

import events.enums.PaymentMethod;
import events.enums.availability.AvailabilityImpact;
import events.enums.availability.AvailabilityRequirement;
import model.Client;
import model.Room;

public class Payment extends Event{
    private final PaymentMethod paymentMethod;

    public Payment(Room room, Client client, PaymentMethod paymentMethod) {
        super(room, client);
        this.paymentMethod = paymentMethod;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    @Override
    public AvailabilityImpact getStatusImpact() {
        return AvailabilityImpact.NONE;
    }

    @Override
    public AvailabilityRequirement getRequirement() {
        return AvailabilityRequirement.REQUIRE_UNAVAILABLE; // you must book/visit a room to pay for them
    }

    @Override
    public String getMessage() {
        return "opłacono";
    }
}
