package avengers.waffle.configuration.messaging.worker.woutbox;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "outbox_result")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OutboxResult {

    @Id
    private String requestId;

    @Lob
    private String payload;

    private LocalDateTime createdAt;


}
