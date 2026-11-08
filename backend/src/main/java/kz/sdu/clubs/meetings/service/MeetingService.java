package kz.sdu.clubs.meetings.service;

import kz.sdu.clubs.meetings.model.Meeting;
import kz.sdu.clubs.meetings.repository.MeetingRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class MeetingService {

    private final MeetingRepository meetingRepository;

    public MeetingService(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    public Meeting createMeeting(Meeting meeting) {
    if (meeting.getDate().isBefore(LocalDate.now())) {
        throw new IllegalArgumentException("Meeting date cannot be in the past");
    }

    return meetingRepository.save(meeting);
}

    public List<Meeting> getAllMeetings() {
        return meetingRepository.findAll();
    }
}