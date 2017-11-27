package ro.bg.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.bg.model.GameReservation;

@Repository
public interface GameReservationDAO extends JpaRepository<GameReservation,Integer> {
}
