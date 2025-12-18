package events;

import events.enums.ClientEventType;
import model.Client;
import model.Room;

public class ClientRoomEvent extends Event{
    private final ClientEventType type;

    public ClientRoomEvent(Room room, Client client, ClientEventType type) {
        super(room, client);
        this.type = type;
    }

    public ClientEventType getType() {
        return type;
    }
}
