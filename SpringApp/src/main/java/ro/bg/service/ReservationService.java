package ro.bg.service;

import ro.bg.model.Event;
import ro.bg.model.Reservation;
import ro.bg.model.dto.ReservationDTO;

import java.util.List;

public interface ReservationService {

    String createReservation(int eventId, int pubId);

    void deleteReservation(Reservation reservation);

    void changeStatus(ReservationDTO reservation);

    List<Event> getWaitingEvents(int pubId);

    List<Event> getAcceptedEvents(int pubId);
}
