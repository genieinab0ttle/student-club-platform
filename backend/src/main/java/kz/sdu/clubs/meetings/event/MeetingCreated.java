package kz.sdu.clubs.meetings.event;

import java.time.Instant;

public record MeetingCreated(
        String eventId,
        Long meetingId,
        Instant occurredAt,
        String title
) {
}
