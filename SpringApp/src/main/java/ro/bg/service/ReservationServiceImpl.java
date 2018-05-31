package ro.bg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.bg.dao.ReservationDAO;
import ro.bg.model.Event;
import ro.bg.model.Pub;
import ro.bg.model.Reservation;
import ro.bg.model.constants.ReservationStatusEnum;

@Service
public class ReservationServiceImpl implements ReservationService{

    @Autowired
    ReservationDAO reservationDAO;

    @Override
    public void changeStatus(Reservation reservation) {
        Reservation oldReservation = reservationDAO.findOne(reservation.getId());
        oldReservation.setMessage(reservation.getMessage());
        oldReservation.setReservationStatusEnum(reservation.getReservationStatusEnum());
        reservationDAO.save(oldReservation);
    }

    @Override
    public String createReservation(int eventId, int pubId) {
        if(reservationDAO.getFreeSeats(eventId) == 0 ){
            if(reservationDAO.getEventReservation(eventId) == null){
                Reservation reservation = new Reservation();
                Pub pub = new Pub();
                pub.setId(pubId);
                Event event = new Event();
                event.setId(eventId);
                reservation.setEvent(event);
                reservation.setPub(pub);
                reservation.setReservationStatusEnum(ReservationStatusEnum.PEDDING);
                reservation.setMessage("Wait response");
                reservationDAO.saveAndFlush(reservation);
                return "Reservation successfully";
            }
            else {
                return "Reservation already exists";
            }
        }
        return "Reservation failure";
    }

    @Override
    public void deleteReservation(Reservation reservation) {
        reservationDAO.delete(reservation.getId());
    }
}
