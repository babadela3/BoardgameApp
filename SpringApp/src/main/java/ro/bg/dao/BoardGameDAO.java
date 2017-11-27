package ro.bg.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.bg.model.BoardGame;

@Repository
public interface BoardGameDAO extends JpaRepository<BoardGame,Integer> {
}
