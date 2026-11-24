package kz.sdu.clubs;

import org.junit.jupiter.api.Test;
import kz.sdu.clubs.meetings.repository.NotificationLogRepository;
import kz.sdu.clubs.meetings.service.MeetingCreated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MeetingCreatedEventTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NotificationLogRepository logRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    // Test 1: Successful reaction
    @Test
    void createMeeting_publishesEvent_andNotificationIsRecorded() throws Exception {
        long before = logRepository.count();
        String date = LocalDate.now().plusDays(7).toString();

        mockMvc.perform(post("/api/meetings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"Chess club","date":"%s","time":"18:00","location":"Room 101"}
                                """.formatted(date)))
                .andExpect(status().isCreated());

        assertEquals(before + 1, logRepository.count());
    }

    // Test 2: Duplicate delivery
    @Test
    void sameEventDeliveredTwice_effectHappensOnce() {
        long before = logRepository.count();

        MeetingCreated event = new MeetingCreated(
                UUID.randomUUID().toString(),
                1L,
                "Chess club",
                LocalDate.now().plusDays(7),
                LocalTime.of(18, 0),
                "Room 101",
                Instant.now()
        );

        eventPublisher.publishEvent(event);
        eventPublisher.publishEvent(event); // same event twicey

        assertEquals(before + 1, logRepository.count());
    }
}