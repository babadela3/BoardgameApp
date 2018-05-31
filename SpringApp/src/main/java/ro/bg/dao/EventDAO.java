package ro.bg.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.bg.model.Event;
import ro.bg.model.EventUser;
import ro.bg.model.User;

import java.util.List;

@Repository
public interface EventDAO extends JpaRepository<Event,Integer> {

    @Query(value = "select * from events\n" +
                    "where data - sysdate() > 0", nativeQuery = true)
    List<Event> findCurrentEvents();

    @Query(value = "select max_seats - (select count(*)\n" +
                                        "from event_users\n" +
                                        "where pk_event_id = :eventId and (status = 'PARTICIPANT' OR STATUS = 'INVITED'))\n" +
                    "from events\n" +
                    "where PK_EVENT_ID = :eventId ;",nativeQuery = true)
    int getFreeSeats(@Param("eventId") int eventId);

    @Query(value = "select * from event_users\n" +
                    "where pk_event_id = :eventId and pk_user_id = :userId ;",nativeQuery = true)
    List<Integer> getEventUserMap(@Param("eventId") int eventId, @Param("userId") int userId);

    @Query(value = "select * from events \n" +
            "where pk_event_id in (select pk_event_id\n" +
            "from event_users\n" +
            "where pk_user_id = :userId)\n" +
            "and data - sysdate() > 0;",nativeQuery = true)
    List<Event> getEventsByUser(@Param("userId") int userId);

    @Query(value = "select status from event_users\n" +
            "where pk_event_id = :eventId and pk_user_id = :userId ;",nativeQuery = true)
    String getUserStatus(@Param("eventId") int eventId, @Param("userId") int userId);

    @Query(value = "select * from users\n" +
            "    where pk_user_id in (select pk_user_id from event_users\n" +
            "            where pk_event_id = :eventId );",nativeQuery = true)
    List<User> getUsers(@Param("eventId") int eventId);


}