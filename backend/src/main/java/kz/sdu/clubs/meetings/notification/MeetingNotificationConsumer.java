package kz.sdu.clubs.meetings.notification;

import kz.sdu.clubs.meetings.event.MeetingCreated;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MeetingNotificationConsumer {

    private final Map<String, MeetingNotification> notifications =
        new ConcurrentHashMap<>();

    @EventListener
    public void handle(MeetingCreated event) {
        MeetingNotification notification = new MeetingNotification(
            event.eventId(),
            event.meetingId(),
            "New event: " + event.title(),
            event.occurredAt()
        );

        notifications.putIfAbsent(event.eventId(), notification);
    }

    public List<MeetingNotification> getNotifications() {
        return List.copyOf(notifications.values());
    }
}