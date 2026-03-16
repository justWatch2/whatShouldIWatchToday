package avengers.waffle.configuration.messaging.outbox;

import lombok.Getter;

@Getter
public class OutboxCreatedEvent {
    private final String requestId;


    public OutboxCreatedEvent(String requestId) {
        this.requestId = requestId;
    }

}
