package avengers.waffle.configuration.messaging.outbox;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxRepository extends JpaRepository<Outbox , Long> {

}
