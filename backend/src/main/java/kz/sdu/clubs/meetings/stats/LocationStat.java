package kz.sdu.clubs.meetings.stats;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "location_stats")
public class LocationStat {

    @Id
    private String location;

    private int meetingCount;

    protected LocationStat() {
    }

    public LocationStat(String location) {
        this.location = location;
        this.meetingCount = 0;
    }

    public void increment() {
        this.meetingCount++;
    }

    public String getLocation() { return location; }
    public int getMeetingCount() { return meetingCount; }
}