package ro.bg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.bg.dao.EventDAO;
import ro.bg.dao.UserDAO;
import ro.bg.model.Event;
import ro.bg.model.EventUser;
import ro.bg.model.User;
import ro.bg.model.constants.StatusUserEnum;

import java.util.*;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    EventDAO eventDAO;

    @Autowired
    UserDAO userDAO;

    @Override
    public List<Event> getAllEvents() {
        List<Event> events =  eventDAO.findAll();
        Collections.sort(events,new Comparator<Event>() {
            @Override
            public int compare(Event ev1, Event ev2) {
                return ev1.getDate().compareTo(ev2.getDate());
            }
        });
        return events;
    }

    @Override
    public List<Event> getCurrentEvents() {
        return eventDAO.findCurrentEvents();
    }

    @Override
    public void createEvent(Event event) {
        event.setUserCreator(userDAO.findOne(event.getUserCreator().getId()));
        Date now = new Date();
        if(event.getUserCreator() != null && event.getDate().compareTo(now) > 0 ){
            eventDAO.saveAndFlush(event);
        }
    }

    @Override
    public void updateEventInformation(Event event) {
        Date now = new Date();
        Event oldEvent = eventDAO.findOne(event.getId());
        oldEvent.setDate(event.getDate());
        oldEvent.setTitle(event.getTitle());
        oldEvent.setDescription(event.getDescription());
        oldEvent.setAddress(event.getAddress());
        oldEvent.setPicture(event.getPicture());
        oldEvent.setMaxSeats(event.getMaxSeats());
        oldEvent.setBoardGames(event.getBoardGames());
        if(oldEvent.getDate().compareTo(now) > 0) {
            eventDAO.save(oldEvent);
        }
    }

    @Override
    public void deleteEvent(Event event) {
        eventDAO.delete(event.getId());
    }

    @Override
    public int getFreeSeats(Event event) {
        return eventDAO.getFreeSeats(event.getId());
    }

    @Override
    public void changeUserStatus(EventUser eventUser) {
        User user = userDAO.findOne(eventUser.getPk().getUser().getId());
        Event event = eventDAO.findOne(eventUser.getPk().getEvent().getId());
        EventUser newEventUser  = new EventUser();
        newEventUser.getPk().setUser(user);
        newEventUser.getPk().setEvent(event);
        newEventUser.setStatusUserEnum(eventUser.getStatusUserEnum());
        event.getEventUserSet().remove(newEventUser);
        event.getEventUserSet().add(newEventUser);
        eventDAO.save(event);

    }
}
