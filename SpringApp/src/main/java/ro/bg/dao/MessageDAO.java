package ro.bg.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.bg.model.Message;

import java.util.List;

@Repository
public interface MessageDAO extends JpaRepository<Message,Integer>{
    @Query(value = "select * from messages\n" +
            "where fk_friendship_id = :friendshipId " +
            "order by data desc;",nativeQuery = true)
    List<Message> getMessages(@Param("friendshipId") int friendshipId);
}
