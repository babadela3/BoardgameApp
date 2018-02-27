package ro.bg.controller;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ro.bg.model.Reservation;
import ro.bg.service.ReservationService;

import java.util.Map;

@Controller
public class ReservationController {

    @Autowired
    ReservationService reservationService;

    @RequestMapping(value = "/createReservation", method = RequestMethod.POST)
    public String createReservation(@RequestBody Reservation reservation) {
        reservationService.createReservation(reservation);
        return "";
    }

    @RequestMapping(value = "/changeReservationStatus", method = RequestMethod.POST)
    public String changeReservationStatus(@RequestBody Reservation reservation) {
        reservationService.changeStatus(reservation);
        return "";
    }

    @RequestMapping(value = "/deleteReservation", method = RequestMethod.POST)
    public String deleteReservation(@RequestBody Reservation reservation) {
        reservationService.deleteReservation(reservation);
        return "";
    }

}
