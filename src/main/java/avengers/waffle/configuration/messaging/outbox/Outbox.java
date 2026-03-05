package avengers.waffle.configuration.messaging.outbox;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "outbox")
public class Outbox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String eventType;

    @Lob
    private String payload;

    private String status;

    private int retryCount;

    private LocalDateTime createdAt;

    private LocalDateTime sentAt;

}
