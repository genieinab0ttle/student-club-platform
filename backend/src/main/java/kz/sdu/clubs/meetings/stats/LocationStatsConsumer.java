package kz.sdu.clubs.meetings.stats;

import jakarta.transaction.Transactional;
import kz.sdu.clubs.meetings.event.MeetingCreated;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class LocationStatsConsumer {

    private final LocationStatRepository statRepository;
    private  final ProcessedEventRepository processedEventRepository;

    public LocationStatsConsumer(LocationStatRepository statRepository,
                                 ProcessedEventRepository processedEventRepository) {
        this.statRepository =statRepository;
        this.processedEventRepository = processedEventRepository;
    }
    @EventListener
    @Transactional
    public void handle(MeetingCreated event) {
        if(processedEventRepository.existsById(event.eventId())) {
            return;
        }
        LocationStat stat = statRepository.findById(event.location())
                .orElseGet(() -> new LocationStat(event.location()));
        stat.increment();
        statRepository.save(stat);

        processedEventRepository.save(new ProcessedEvent(event.eventId()));
    }
}
