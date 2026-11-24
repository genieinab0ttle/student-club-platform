package kz.sdu.clubs.meetings.event;

import java.time.Instant;

public record MeetingCreated(
    String eventId,
    Long meetingId,
    String title,
    Instant occurredAt
) {}