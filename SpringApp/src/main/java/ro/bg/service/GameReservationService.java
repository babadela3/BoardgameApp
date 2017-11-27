package ro.bg.service;


import ro.bg.model.GameReservation;

public interface GameReservationService {

    void createGameReservation(GameReservation gameReservation);

    void updateGameReservation(GameReservation gameReservation);

    void changeStatus(GameReservation gameReservation);

}
