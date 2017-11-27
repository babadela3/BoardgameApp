package ro.bg.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.bg.model.Friendship;

@Repository
public interface FriendshipDAO extends JpaRepository<Friendship,Integer>{
}
