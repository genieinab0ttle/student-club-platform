package kz.sdu.clubs.meetings.confirmation;

import kz.sdu.clubs.meetings.event.MeetingCreated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MeetingCreatedConfirmationConsumer {

    private static final Logger logger =
            LoggerFactory.getLogger(MeetingCreatedConfirmationConsumer.class);

    private final Set<String> processedEventIds = ConcurrentHashMap.newKeySet();

    @EventListener
    public void handle(MeetingCreated event) {
        if (!processedEventIds.add(event.eventId())) {
            return;
        }

        logger.info("Meeting creation confirmed: id={}, title={}",
                event.meetingId(), event.title());
    }
}
