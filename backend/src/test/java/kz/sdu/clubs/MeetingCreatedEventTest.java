package kz.sdu.clubs;

import kz.sdu.clubs.meetings.confirmation.MeetingCreatedConfirmationConsumer;
import kz.sdu.clubs.meetings.event.MeetingCreated;
import kz.sdu.clubs.meetings.model.Meeting;
import kz.sdu.clubs.meetings.repository.MeetingRepository;
import kz.sdu.clubs.meetings.service.MeetingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import java.time.Instant;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(OutputCaptureExtension.class)
class MeetingCreatedEventTest {

    @Test
    void successfulMeetingCreationPublishesEventAfterSaving() {
        MeetingRepository repository = mock(MeetingRepository.class);
        ApplicationEventPublisher eventPublisher = mock(ApplicationEventPublisher.class);
        Meeting request = mock(Meeting.class);
        Meeting savedMeeting = mock(Meeting.class);
        when(request.getDate()).thenReturn(LocalDate.now().plusDays(30));
        when(repository.save(request)).thenReturn(savedMeeting);
        when(savedMeeting.getId()).thenReturn(102L);
        when(savedMeeting.getTitle()).thenReturn("Team meeting");

        MeetingService meetingService = new MeetingService(repository, eventPublisher);
        meetingService.createMeeting(request);

        ArgumentCaptor<Object> eventCaptor = ArgumentCaptor.forClass(Object.class);
        InOrder inOrder = inOrder(repository, eventPublisher);
        inOrder.verify(repository).save(request);
        inOrder.verify(eventPublisher).publishEvent(eventCaptor.capture());
        MeetingCreated event = assertInstanceOf(MeetingCreated.class, eventCaptor.getValue());
        assertEquals(102L, event.meetingId());
        assertEquals("Team meeting", event.title());
        assertNotNull(event.eventId());
        assertNotNull(event.occurredAt());
    }

    @Test
    void duplicateDeliveryIsConfirmedOnlyOnce(CapturedOutput output) {
        MeetingCreated event = new MeetingCreated(
                "duplicate-event-1",
                102L,
                Instant.now(),
                "Duplicate meeting"
        );

        MeetingCreatedConfirmationConsumer consumer =
                new MeetingCreatedConfirmationConsumer();
        consumer.handle(event);
        consumer.handle(event);

        String confirmation = "Meeting creation confirmed: id=102, title=Duplicate meeting";
        assertEquals(1, output.getAll().split(confirmation, -1).length - 1);
    }
}
