package kz.sdu.clubs.meetings.service;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import kz.sdu.clubs.meetings.model.NotificationLog;
import kz.sdu.clubs.meetings.repository.NotificationLogRepository;

@Component
public class MeetingCreatedNotificationConsumer {

    private final NotificationLogRepository logRepository;
    private final NotificationSender sender;

    public MeetingCreatedNotificationConsumer(NotificationLogRepository logRepository,
                                              NotificationSender sender) {
        this.logRepository = logRepository;
        this.sender = sender;
    }

    @EventListener
    public void handle(MeetingCreated event) {
        if (logRepository.existsByEventId(event.eventId())) {
            return;
        }

        String text = "New club meeting: " + event.title()
                + "\n" + event.date() + " " + event.time()
                + "\nLocation: " + event.location();

        logRepository.save(new NotificationLog(
                event.eventId(),
                "MEETING_CREATED",
                event.meetingId(),
                "TELEGRAM",
                text,
                event.occurredAt()
        ));

        sender.send(text);
    }
}