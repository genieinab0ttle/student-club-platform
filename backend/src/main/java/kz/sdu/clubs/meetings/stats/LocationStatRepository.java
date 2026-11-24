package kz.sdu.clubs.meetings.stats;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationStatRepository extends JpaRepository<LocationStat, String> {
}