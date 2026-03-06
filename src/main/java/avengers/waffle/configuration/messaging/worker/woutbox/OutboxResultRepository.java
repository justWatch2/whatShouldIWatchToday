package avengers.waffle.configuration.messaging.worker.woutbox;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxResultRepository extends JpaRepository<OutboxResult, String> {

}
