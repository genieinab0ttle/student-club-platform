package kz.sdu.clubs;

import kz.sdu.clubs.meetings.event.MeetingCreated;
import kz.sdu.clubs.meetings.model.Meeting;
import kz.sdu.clubs.meetings.service.MeetingService;
import kz.sdu.clubs.meetings.stats.LocationStatRepository;
import kz.sdu.clubs.meetings.stats.ProcessedEventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MeetingStatsTest {

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private LocationStatRepository statRepository;

    @Autowired
    private ProcessedEventRepository processedEventRepository;

    @Test
    void createMeetingIncreasesLocationCount() {
        String location = "Test Room " + UUID.randomUUID();

        Meeting meeting = new Meeting();
        meeting.setTitle("Club meeting");
        meeting.setDate(LocalDate.now().plusDays(1));
        meeting.setTime(LocalTime.of(15, 0));
        meeting.setLocation(location);

        Meeting saved = meetingService.createMeeting(meeting);

        assertNotNull(saved.getId());
        assertEquals(1, statRepository.findById(location).orElseThrow().getMeetingCount());
    }

    @Test
    void sameEventTwiceIsCountedOnce() {
        String location = "Test Room " + UUID.randomUUID();
        MeetingCreated event = new MeetingCreated(
                UUID.randomUUID().toString(), 99L, location, Instant.now());

        eventPublisher.publishEvent(event);
        eventPublisher.publishEvent(event);

        assertEquals(1, statRepository.findById(location).orElseThrow().getMeetingCount());
        assertTrue(processedEventRepository.existsById(event.eventId()));
    }
}