package ro.bg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.bg.dao.GameReservationDAO;
import ro.bg.model.GameReservation;
import ro.bg.model.constants.ReservationStatusEnum;

@Service
public class GameReservationServiceImpl implements GameReservationService{

    @Autowired
    GameReservationDAO gameReservationDAO;

    @Override
    public void createGameReservation(GameReservation gameReservation) {
        gameReservationDAO.saveAndFlush(gameReservation);
    }

    @Override
    public void updateGameReservation(GameReservation gameReservation) {
        GameReservation gameReservation1 = gameReservationDAO.getOne(gameReservation.getId());
        gameReservation1.setEndDate(gameReservation.getEndDate());
        gameReservation1.setStartDate(gameReservation.getStartDate());
        gameReservation1.setReservationStatusEnum(ReservationStatusEnum.PEDDING);
        gameReservation1.setMessage(gameReservation.getMessage());
        gameReservationDAO.save(gameReservation1);
    }

    @Override
    public void changeStatus(GameReservation gameReservation) {
        GameReservation gameReservation1 = gameReservationDAO.getOne(gameReservation.getId());
        gameReservation1.setReservationStatusEnum(gameReservation.getReservationStatusEnum());
        gameReservationDAO.save(gameReservation1);
    }
}
