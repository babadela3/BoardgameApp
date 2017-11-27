package ro.bg.service;

import ro.bg.model.Reservation;

public interface ReservationService {

    void createReservation(Reservation reservation);

    void deleteReservation(Reservation reservation);

    void changeStatus(Reservation reservation);
}
