package ro.bg.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.bg.model.FriendshipRequest;

import java.util.List;

@Repository
public interface FriendshipRequestDAO extends JpaRepository<FriendshipRequest,Integer>{

    @Query(value = "select * from friendship_requests\n" +
            "where FK_RECEIVER_ID = :userId " +
            "order by PK_FRIENDSHIP_REQUEST_ID desc;", nativeQuery = true)
    List<FriendshipRequest> findReceiveRequests(@Param("userId") int userId);

    @Query(value = "select * from friendship_requests\n" +
            "where (FK_SENDER_ID = :senderId and FK_RECEIVER_ID = :receiverId )" +
            "or (FK_SENDER_ID = :receiverId and FK_RECEIVER_ID = :senderId );", nativeQuery = true)
    FriendshipRequest findRequest(@Param("senderId") int senderId, @Param("receiverId") int receiverId);

    @Query(value = "select * from friendship_requests\n" +
            "where (FK_SENDER_ID = :senderId and FK_RECEIVER_ID = :receiverId );", nativeQuery = true)
    FriendshipRequest getRequest(@Param("senderId") int senderId, @Param("receiverId") int receiverId);
}
