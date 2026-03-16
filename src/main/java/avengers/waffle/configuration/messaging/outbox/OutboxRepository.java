package avengers.waffle.configuration.messaging.outbox;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OutboxRepository extends JpaRepository<Outbox , String> {

    List<Outbox> findByStatusAndCreatedAtBeforeOrderByCreatedAtAsc(
            String status,
            LocalDateTime threshold,
            Pageable pageable
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT o FROM Outbox o WHERE o.requestId = :requestId")
    Optional<Outbox> findByIdWithLock(@Param("requestId") String requestId);

}
