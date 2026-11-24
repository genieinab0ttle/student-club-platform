package kz.sdu.clubs.meetings.repository;

import kz.sdu.clubs.meetings.model.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {

    boolean existsByEventId(String eventId);
}