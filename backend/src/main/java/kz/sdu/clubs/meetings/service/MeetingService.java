package kz.sdu.clubs.meetings.service;

import kz.sdu.clubs.meetings.model.Meeting;
import kz.sdu.clubs.meetings.repository.MeetingRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import java.time.Instant;
import java.util.UUID;

@Service
public class MeetingService {
    private final MeetingRepository meetingRepository;
    private final ApplicationEventPublisher eventPublisher;

    public MeetingService(MeetingRepository meetingRepository,
                          ApplicationEventPublisher eventPublisher) {
        this.meetingRepository = meetingRepository;
        this.eventPublisher = eventPublisher;
    }

    public Meeting createMeeting(Meeting meeting) {
    if (meeting.getDate().isBefore(LocalDate.now())) {
        throw new IllegalArgumentException("Meeting date cannot be in the past");
    }

    Meeting saved = meetingRepository.save(meeting);

    eventPublisher.publishEvent(new MeetingCreated(
        UUID.randomUUID().toString(),
        saved.getId(),
        saved.getTitle(),
        saved.getDate(),
        saved.getTime(),
        saved.getLocation(),
        Instant.now()
    ));

    return saved;
}

    public List<Meeting> getAllMeetings() {
        return meetingRepository.findAll();
    }
}