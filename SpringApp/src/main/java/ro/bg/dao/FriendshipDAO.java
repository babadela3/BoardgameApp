package ro.bg.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.bg.model.Friendship;

@Repository
public interface FriendshipDAO extends JpaRepository<Friendship,Integer>{

    @Query(value = "SELECT * FROM FRIENDSHIPS " +
            "WHERE (FK_FRIEND_ONE_ID = :friendOne AND FK_FRIEND_TWO_ID = :friendTwo)" +
            "OR (FK_FRIEND_ONE_ID = :friendTwo AND FK_FRIEND_TWO_ID = :friendOne)", nativeQuery = true)
    Friendship getFriendship(@Param("friendOne") int friendOne, @Param("friendTwo") int friendTwo);
}
