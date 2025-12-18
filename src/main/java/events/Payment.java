package events;

import events.enums.PaymentMethod;
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
}
