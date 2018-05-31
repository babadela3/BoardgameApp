package ro.bg.service;

import ro.bg.model.Reservation;

public interface ReservationService {

    String createReservation(int eventId, int pubId);

    void deleteReservation(Reservation reservation);

    void changeStatus(Reservation reservation);
}
