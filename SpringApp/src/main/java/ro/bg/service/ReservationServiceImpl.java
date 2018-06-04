package ro.bg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ro.bg.dao.EventDAO;
import ro.bg.dao.ReservationDAO;
import ro.bg.dao.UserDAO;
import ro.bg.model.Event;
import ro.bg.model.Pub;
import ro.bg.model.Reservation;
import ro.bg.model.User;
import ro.bg.model.constants.ReservationStatusEnum;
import ro.bg.model.dto.ReservationDTO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService{

    @Autowired
    ReservationDAO reservationDAO;

    @Autowired
    EventDAO eventDAO;

    @Autowired
    UserDAO userDAO;

    @Override
    public void changeStatus(ReservationDTO reservation) {
        Reservation oldReservation = reservationDAO.getEventReservation(reservation.getId());
        oldReservation.setMessage(reservation.getMessage());
        oldReservation.setReservationStatusEnum(ReservationStatusEnum.valueOf(reservation.getOption()));
        reservationDAO.save(oldReservation);
    }

    @Override
    public List<Event> getWaitingEvents(int pubId) {
        List<Event> events = new ArrayList<>();
        List<Reservation> reservations = reservationDAO.getReservationsByPub(pubId);
        for(Reservation reservation :  reservations) {
            Event event = new Event();
            Event eventDB = eventDAO.findOne(reservation.getEvent().getId());
            event.setId(eventDB.getId());
            event.setTitle(eventDB.getTitle());
            event.setDescription(eventDB.getDescription());
            List<Integer> ids = eventDAO.getUsersId(eventDB.getId());
            List<User> userList = new ArrayList<>();
            for(Integer id  : ids) {
                User user = new User();
                userList.add(user);
            }
            event.setUsers(new HashSet<>(userList));
            event.setBoardGames(eventDB.getBoardGames());
            event.setDate(eventDB.getDate());
            event.setUserCreator(eventDB.getUserCreator());
            events.add(event);
        }
        return events;
    }

    @Override
    public List<Event> getAcceptedEvents(int pubId) {
        List<Event> events = new ArrayList<>();
        List<Reservation> reservations = reservationDAO.getReservations(pubId);
        for(Reservation reservation :  reservations) {
            Event event = new Event();
            Event eventDB = eventDAO.findOne(reservation.getEvent().getId());
            event.setId(eventDB.getId());
            event.setTitle(eventDB.getTitle());
            event.setDescription(eventDB.getDescription());
            List<Integer> ids = eventDAO.getUsersId(eventDB.getId());
            List<User> userList = new ArrayList<>();
            for(Integer id  : ids) {
                User user = new User();
                userList.add(user);
            }
            event.setUsers(new HashSet<>(userList));
            event.setBoardGames(eventDB.getBoardGames());
            event.setDate(eventDB.getDate());
            event.setUserCreator(eventDB.getUserCreator());
            events.add(event);
        }
        return events;
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
