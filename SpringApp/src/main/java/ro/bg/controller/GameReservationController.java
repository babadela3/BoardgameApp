package ro.bg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ro.bg.model.GameReservation;
import ro.bg.service.GameReservationService;

@Controller
public class GameReservationController {

    @Autowired
    GameReservationService gameReservationService;

    @RequestMapping(value = "/createGameReservation", method = RequestMethod.POST)
    public String createGameReservation(@RequestBody GameReservation gameReservation) {
        gameReservationService.createGameReservation(gameReservation);
        return "";
    }

    @RequestMapping(value = "/updateGameReservation", method = RequestMethod.POST)
    public String updateGameReservation(@RequestBody GameReservation gameReservation) {
        gameReservationService.updateGameReservation(gameReservation);
        return "";
    }

    @RequestMapping(value = "/changeStatusGameReservation", method = RequestMethod.POST)
    public String changeStatus(@RequestBody GameReservation gameReservation) {
        gameReservationService.changeStatus(gameReservation);
        return "";
    }
}
