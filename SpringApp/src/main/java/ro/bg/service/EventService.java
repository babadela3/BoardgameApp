package ro.bg.service;

import ro.bg.model.Event;
import ro.bg.model.EventUser;

import java.util.List;

public interface EventService {

    List<Event> getAllEvents();

    List<Event> getCurrentEvents();

    void createEvent(Event event);

    void updateEventInformation(Event event);

    void deleteEvent(Event event);

    int getFreeSeats(Event event);

    void changeUserStatus(EventUser eventUser);
}
