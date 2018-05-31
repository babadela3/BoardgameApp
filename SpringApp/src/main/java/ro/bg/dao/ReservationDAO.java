package ro.bg.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.bg.model.Reservation;

@Repository
public interface ReservationDAO extends JpaRepository<Reservation, Integer>{
    @Query(value = "select max_seats - (select count(*)\n" +
            "from event_users\n" +
            "where pk_event_id = :eventId and status = 'PARTICIPANT')\n" +
            "from events\n" +
            "where PK_EVENT_ID = :eventId ;",nativeQuery = true)
    int getFreeSeats(@Param("eventId") int eventId);

    @Query(value = "select * from reservations " +
            "where fk_event_id = :eventId ;",nativeQuery = true)
    Reservation getEventReservation(@Param("eventId") int eventId);
}
