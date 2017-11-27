package ro.bg.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.bg.model.Notification;

import java.util.List;

@Repository
public interface NotificationDAO extends JpaRepository<Notification,Integer>{
    @Query(value = "select * from notifications\n" +
            "where fk_user_id = :userId " +
            "order by data desc;",nativeQuery = true)
    List<Notification> getNotifications(@Param("userId") int userId);
}
