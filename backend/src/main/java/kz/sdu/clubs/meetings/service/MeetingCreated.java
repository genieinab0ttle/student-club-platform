package kz.sdu.clubs.meetings.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

public record MeetingCreated(
        String eventId,
        Long meetingId,
        String title,
        LocalDate date,
        LocalTime time,
        String location,
        Instant occurredAt
) {}