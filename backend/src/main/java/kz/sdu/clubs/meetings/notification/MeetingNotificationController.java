package kz.sdu.clubs.meetings.notification;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/notifications")
public class MeetingNotificationController {

    private final MeetingNotificationConsumer consumer;

    public MeetingNotificationController(
            MeetingNotificationConsumer consumer
    ) {
        this.consumer = consumer;
    }

    @GetMapping
    public List<MeetingNotification> getNotifications() {
        return consumer.getNotifications();
    }
}