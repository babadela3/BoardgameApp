package ro.bg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.bg.dao.ReservationDAO;
import ro.bg.model.Reservation;

@Service
public class ReservationServiceImpl implements ReservationService{

    @Autowired
    ReservationDAO reservationDAO;

    @Override
    public void createReservation(Reservation reservation) {
        reservationDAO.saveAndFlush(reservation);
    }

    @Override
    public void changeStatus(Reservation reservation) {
        Reservation oldReservation = reservationDAO.findOne(reservation.getId());
        oldReservation.setMessage(reservation.getMessage());
        oldReservation.setReservationStatusEnum(reservation.getReservationStatusEnum());
        reservationDAO.save(oldReservation);
    }

    @Override
    public void deleteReservation(Reservation reservation) {
        reservationDAO.delete(reservation.getId());
    }
}
