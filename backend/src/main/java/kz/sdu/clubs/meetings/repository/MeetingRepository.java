package kz.sdu.clubs.meetings.repository;

import kz.sdu.clubs.meetings.model.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
}