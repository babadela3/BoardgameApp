package ro.bg.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.bg.model.Reservation;

@Repository
public interface ReservationDAO extends JpaRepository<Reservation, Integer>{
}
