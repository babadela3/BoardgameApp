package ro.bg.service;

import ro.bg.model.Event;
import ro.bg.model.EventUser;
import ro.bg.model.Pub;
import ro.bg.model.User;
import ro.bg.model.dto.BoardGameAndroidDTO;
import ro.bg.model.dto.EventDTO;
import ro.bg.model.dto.FriendAndroidDTO;
import ro.bg.model.dto.UserParticipantDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface EventService {

    List<Event> getAllEvents();

    List<Event> getCurrentEvents();

    void createEvent(Event event);

    void updateEventInformation(Event event);

    void deleteEvent(Event event);

    int getFreeSeats(Event event);

    void changeUserStatus(EventUser eventUser);

    void createEvent(String name, String description, int maxPlayers, ArrayList<FriendAndroidDTO> friends, ArrayList<BoardGameAndroidDTO> games, Date date,
                     Pub pub, String address, double latitude, double longitude, User userCreator);

    List<EventDTO> getEventsByUserId(int userId);

    EventDTO getEvent(int eventId);

    List<EventDTO> getEventsByName(String name);

    List<UserParticipantDTO> getUsers(int eventId);

    String changeStatus(int eventId, int userId, String option);
}
