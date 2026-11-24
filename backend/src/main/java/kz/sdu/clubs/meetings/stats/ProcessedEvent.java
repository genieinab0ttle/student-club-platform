package kz.sdu.clubs.meetings.stats;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "processed_envents")
public class ProcessedEvent {

    @Id
    private String enventId;
    private Instant processedAt;
    protected ProcessedEvent() {
    }
    public ProcessedEvent(String enventId) {
        this.enventId = enventId;
        this.processedAt = Instant.now();
    }
    public String getEnventId() {return enventId;}
    public Instant getProcessedAt() {return processedAt;}
}

