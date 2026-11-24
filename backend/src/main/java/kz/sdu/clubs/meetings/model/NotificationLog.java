package kz.sdu.clubs.meetings.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "notification_log",
        uniqueConstraints = @UniqueConstraint(columnNames = "event_id"))
public class NotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false)
    private String eventId;

    private String type;
    private Long meetingId;
    private String channel;

    @Column(length = 1000)
    private String message;

    private Instant createdAt;

    protected NotificationLog() {
    }

    public NotificationLog(String eventId, String type, Long meetingId,
                           String channel, String message, Instant createdAt) {
        this.eventId = eventId;
        this.type = type;
        this.meetingId = meetingId;
        this.channel = channel;
        this.message = message;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getEventId() { return eventId; }
    public String getType() { return type; }
    public Long getMeetingId() { return meetingId; }
    public String getChannel() { return channel; }
    public String getMessage() { return message; }
    public Instant getCreatedAt() { return createdAt; }
}