package kz.sdu.clubs.meetings.notification;

import java.time.Instant;

public record MeetingNotification(
    String eventId,
    Long meetingId,
    String message,
    Instant createdAt
) {}