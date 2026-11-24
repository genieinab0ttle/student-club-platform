package kz.sdu.clubs;

import kz.sdu.clubs.meetings.event.MeetingCreated;
import kz.sdu.clubs.meetings.model.Meeting;
import kz.sdu.clubs.meetings.notification.MeetingNotificationConsumer;
import kz.sdu.clubs.meetings.repository.MeetingRepository;
import kz.sdu.clubs.meetings.service.MeetingService;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MeetingNotificationTest {

    @Test
    void successfulCreationPublishesEventAndCreatesNotification() {
        MeetingRepository repository = mock(MeetingRepository.class);

        try (var context = new AnnotationConfigApplicationContext()) {
            context.registerBean(MeetingRepository.class, () -> repository);
            context.register(
                MeetingService.class,
                MeetingNotificationConsumer.class
            );

            List<MeetingCreated> events = new ArrayList<>();

            context.addApplicationListener(event -> {
                if (event instanceof
                        org.springframework.context.PayloadApplicationEvent<?> payload
                        && payload.getPayload() instanceof MeetingCreated created) {
                    events.add(created);
                }
            });

            context.refresh();

            Meeting request = new Meeting();
            request.setTitle("Winx Meeting");
            request.setDate(LocalDate.now().plusDays(1));
            request.setTime(LocalTime.of(14, 0));
            request.setLocation("Room 204");

            Meeting saved = mock(Meeting.class);
            when(saved.getId()).thenReturn(42L);
            when(saved.getTitle()).thenReturn("Winx Meeting");
            when(repository.save(request)).thenReturn(saved);

            MeetingService service = context.getBean(MeetingService.class);
            MeetingNotificationConsumer consumer =
                context.getBean(MeetingNotificationConsumer.class);

            Instant before = Instant.now();
            Meeting result = service.createMeeting(request);

            assertSame(saved, result);
            verify(repository).save(request);

            assertEquals(1, events.size());

            MeetingCreated event = events.get(0);

            assertEquals(Long.valueOf(42L), event.meetingId());
            assertEquals("Winx Meeting", event.title());
            assertDoesNotThrow(() -> UUID.fromString(event.eventId()));
            assertFalse(event.occurredAt().isBefore(before));
            assertFalse(event.occurredAt().isAfter(Instant.now()));

            assertEquals(1, consumer.getNotifications().size());

            var notification = consumer.getNotifications().get(0);

            assertEquals(event.eventId(), notification.eventId());
            assertEquals(event.meetingId(), notification.meetingId());
            assertEquals("New event: Winx Meeting", notification.message());
            assertEquals(event.occurredAt(), notification.createdAt());
        }
    }

    @Test
    void duplicateEventCreatesOnlyOneNotification() {
        try (var context = new AnnotationConfigApplicationContext()) {
            context.register(MeetingNotificationConsumer.class);
            context.refresh();

            MeetingCreated event = new MeetingCreated(
                UUID.randomUUID().toString(),
                42L,
                "Winx Meeting",
                Instant.now()
            );

            context.publishEvent(event);
            context.publishEvent(event);

            MeetingNotificationConsumer consumer =
                context.getBean(MeetingNotificationConsumer.class);

            assertEquals(1, consumer.getNotifications().size());

            var notification = consumer.getNotifications().get(0);

            assertEquals(event.eventId(), notification.eventId());
            assertEquals(event.meetingId(), notification.meetingId());
            assertEquals("New event: Winx Meeting", notification.message());
            assertEquals(event.occurredAt(), notification.createdAt());
        }
    }
}