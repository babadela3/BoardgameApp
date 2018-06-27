package ro.bg.controller;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ro.bg.model.Pub;
import ro.bg.model.Reservation;
import ro.bg.model.dto.ReservationDTO;
import ro.bg.service.ReservationService;

import java.util.Map;

@Controller
public class ReservationController {

    @Autowired
    ReservationService reservationService;

    @RequestMapping(value = "/createReservation", method = RequestMethod.POST)
    public ResponseEntity<Object> createReservation(@ModelAttribute("eventId") String eventId,
                                                    @ModelAttribute("pubId") String pubId) {
        return ResponseEntity.status(HttpStatus.OK).body(reservationService.createReservation(Integer.parseInt(eventId),Integer.parseInt(pubId)));
    }

    @RequestMapping(value = "/changeReservationStatus", method = RequestMethod.POST)
    public ResponseEntity<Object> changeReservationStatus(@RequestBody ReservationDTO reservationDTO) {
        reservationService.changeStatus(reservationDTO);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @RequestMapping(value = "/deleteReservation", method = RequestMethod.POST)
    public String deleteReservation(@RequestBody Reservation reservation) {
        reservationService.deleteReservation(reservation);
        return "";
    }

}
