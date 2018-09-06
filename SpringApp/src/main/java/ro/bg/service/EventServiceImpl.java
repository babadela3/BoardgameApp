package ro.bg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ro.bg.dao.*;
import ro.bg.model.*;
import ro.bg.model.constants.NotificationTypeEnum;
import ro.bg.model.constants.StatusUserEnum;
import ro.bg.model.dto.BoardGameAndroidDTO;
import ro.bg.model.dto.EventDTO;
import ro.bg.model.dto.FriendAndroidDTO;
import ro.bg.model.dto.UserParticipantDTO;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    EventDAO eventDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    BoardGameDAO boardGameDAO;

    @Autowired
    PubDAO pubDAO;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    NotificationDAO notificationDAO;


    @Override
    public List<Event> getAllEvents() {
        List<Event> events =  eventDAO.findCurrentEvents();
        Collections.sort(events,new Comparator<Event>() {
            @Override
            public int compare(Event ev1, Event ev2) {
                return ev1.getDate().compareTo(ev2.getDate());
            }
        });
        for(Event event : events) {
            event.setReservations(null);
            event.setEventUserSet(null);
            event.setUsers(null);
            event.setNotifications(null);
            User user = new User();
            user.setId(event.getUserCreator().getId());
            user.setName(event.getUserCreator().getName());
            event.setUserCreator(user);

            List<BoardGame> boardGames = new ArrayList<>(event.getBoardGames());
            for(BoardGame boardGame : boardGames){
                boardGame.setGameReservations(null);
                boardGame.setPubs(null);
                boardGame.setEvents(null);
                boardGame.setUsers(null);
            }
            event.setBoardGames(new HashSet<>(boardGames));
        }
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

    @Override
    public void createEvent(String name, String description, int maxPlayers, ArrayList<FriendAndroidDTO> friends, ArrayList<BoardGameAndroidDTO> games, Date date, Pub pub, String address, double latitude, double longitude, User userCreator) {
        Event event = new Event();
        event.setTitle(name);
        event.setDescription(description);
        event.setMaxSeats(maxPlayers + 1);
        List<BoardGame> boardGames = new ArrayList<>();
        for(BoardGameAndroidDTO boardGameAndroidDTO :  games) {
            BoardGame boardGame = boardGameDAO.findOne(boardGameAndroidDTO.getId());
            boardGame.getId();
            boardGames.add(boardGame);
        }
        event.setBoardGames(new HashSet<>(boardGames));
        event.setDate(date);
        event.setAddress(address);
        if(pub == null){
            event.setLatitude(latitude);
            event.setLongitude(longitude);
        }
        event.setUserCreator(userCreator);
        if(games.size() > 1){
            event.setPicture("null");
        }
        else {
            event.setPicture(games.get(0).getPicture());
        }


        List<User> users = new ArrayList<>();
        List<EventUser> eventUsers = new ArrayList<>();

        if(friends != null) {
            for (FriendAndroidDTO friendAndroidDTO : friends) {
                User user = new User();
                user.setId(friendAndroidDTO.getId());
                users.add(user);

                EventUserId eventUserId = new EventUserId();
                eventUserId.setEvent(event);
                eventUserId.setUser(user);
                EventUser eventUser = new EventUser();
                eventUser.setPk(eventUserId);
                eventUser.setStatusUserEnum(StatusUserEnum.INVITED);
                eventUsers.add(eventUser);

                Notification notification = new Notification();
                notification.setNotificationTypeEnum(NotificationTypeEnum.INVITATION_EVENT);
                notification.setUser(userDAO.findOne(friendAndroidDTO.getId()));
                notification.setDate(new Date());
                notification.setMessage("You have been invited to " + name + " by " + userDAO.findOne(userCreator.getId()).getName());
                notificationDAO.saveAndFlush(notification);
            }

        }
        User user = new User();
        user.setId(userCreator.getId());
        users.add(user);
        event.setUsers(new HashSet<User>(users));

        EventUserId eventUserId = new EventUserId();
        eventUserId.setEvent(event);
        eventUserId.setUser(userCreator);
        EventUser eventUser = new EventUser();
        eventUser.setPk(eventUserId);
        eventUser.setStatusUserEnum(StatusUserEnum.PARTICIPANT);
        eventUsers.add(eventUser);

        event.setEventUserSet(new HashSet<>(eventUsers));
        eventDAO.saveAndFlush(event);
    }

    @Override
    public List<EventDTO> getEventsByUserId(int userId) {

        List<EventDTO> eventDTOS = new ArrayList<>();
        List<Event> events = eventDAO.getEventsByUser(userId);
        for(Event event : events) {
            EventDTO eventDTO = new EventDTO();

            eventDTO.setId(event.getId());
            eventDTO.setTitle(event.getTitle());
            eventDTO.setDescription(event.getDescription());
            eventDTO.setAddress(event.getAddress());
            eventDTO.setDate(new SimpleDateFormat("HH:mm dd.MM.yyyy").format(event.getDate()));

            User userCreator = new User();
            userCreator.setId(event.getUserCreator().getId());
            userCreator.setName(event.getUserCreator().getName());
            eventDTO.setUserCreator(userCreator);

            eventDTO.setPicture(event.getPicture());
            eventDTO.setMaxSeats(event.getMaxSeats());
            eventDTO.setLatitude(event.getLatitude());
            eventDTO.setLongitude(event.getLongitude());

            List<BoardGame> boardGames = new ArrayList<>();
            for(BoardGame boardGame : event.getBoardGames()){
                BoardGame boardGameDTO = new BoardGame();
                boardGameDTO.setId(boardGame.getId());
                boardGameDTO.setName(boardGame.getName());
                boardGameDTO.setDescription(boardGame.getDescription());
                boardGameDTO.setPicture(boardGame.getPicture());
                boardGames.add(boardGameDTO);
            }
            eventDTO.setBoardGames(boardGames);

            Pub pub = pubDAO.findByAddress(event.getAddress());
            Pub pubDTO = new Pub();
            if(pub != null) {
                pubDTO.setId(pub.getId());
                pubDTO.setName(pub.getName());
                eventDTO.setPub(pubDTO);
            }

            eventDTOS.add(eventDTO);
        }
        return eventDTOS;
    }

    @Override
    public EventDTO getEvent(int eventId) {
        Event event = eventDAO.findOne(eventId);
        EventDTO eventDTO = new EventDTO();

        eventDTO.setId(event.getId());
        eventDTO.setTitle(event.getTitle());
        eventDTO.setDescription(event.getDescription());
        eventDTO.setAddress(event.getAddress());
        eventDTO.setDate(new SimpleDateFormat("HH:mm dd.MM.yyyy").format(event.getDate()));

        User userCreator = new User();
        userCreator.setId(event.getUserCreator().getId());
        userCreator.setName(event.getUserCreator().getName());
        eventDTO.setUserCreator(userCreator);

        eventDTO.setPicture(event.getPicture());
        eventDTO.setMaxSeats(event.getMaxSeats());
        eventDTO.setLatitude(event.getLatitude());
        eventDTO.setLongitude(event.getLongitude());

        List<BoardGame> boardGames = new ArrayList<>();
        for(BoardGame boardGame : event.getBoardGames()){
            BoardGame boardGameDTO = new BoardGame();
            boardGameDTO.setId(boardGame.getId());
            boardGameDTO.setName(boardGame.getName());
            boardGameDTO.setDescription(boardGame.getDescription());
            boardGameDTO.setPicture(boardGame.getPicture());
            boardGames.add(boardGameDTO);
        }
        eventDTO.setBoardGames(boardGames);

        Pub pub = pubDAO.findByAddress(event.getAddress());
        Pub pubDTO = new Pub();
        if(pub != null) {
            pubDTO.setId(pub.getId());
            pubDTO.setName(pub.getName());
            eventDTO.setPub(pubDTO);
        }
        return eventDTO;
    }

    @Override
    public List<EventDTO> getEventsByName(String name) {
        List<EventDTO> eventDTOS = new ArrayList<>();
        List<Event> events = eventDAO.findByTitleContaining(name);
        for(Event event : events) {
            EventDTO eventDTO = new EventDTO();

            eventDTO.setId(event.getId());
            eventDTO.setTitle(event.getTitle());
            eventDTO.setDescription(event.getDescription());
            eventDTO.setAddress(event.getAddress());
            eventDTO.setDate(new SimpleDateFormat("HH:mm dd.MM.yyyy").format(event.getDate()));

            User userCreator = new User();
            userCreator.setId(event.getUserCreator().getId());
            userCreator.setName(event.getUserCreator().getName());
            eventDTO.setUserCreator(userCreator);

            eventDTO.setPicture(event.getPicture());
            eventDTO.setMaxSeats(event.getMaxSeats());
            eventDTO.setLatitude(event.getLatitude());
            eventDTO.setLongitude(event.getLongitude());

            List<BoardGame> boardGames = new ArrayList<>();
            for(BoardGame boardGame : event.getBoardGames()){
                BoardGame boardGameDTO = new BoardGame();
                boardGameDTO.setId(boardGame.getId());
                boardGameDTO.setName(boardGame.getName());
                boardGameDTO.setDescription(boardGame.getDescription());
                boardGameDTO.setPicture(boardGame.getPicture());
                boardGames.add(boardGameDTO);
            }
            eventDTO.setBoardGames(boardGames);

            Pub pub = pubDAO.findByAddress(event.getAddress());
            Pub pubDTO = new Pub();
            if(pub != null) {
                pubDTO.setId(pub.getId());
                pubDTO.setName(pub.getName());
                eventDTO.setPub(pubDTO);
            }

            eventDTOS.add(eventDTO);
        }
        return eventDTOS;
    }

    @Override
    public List<UserParticipantDTO> getUsers(int eventId) {
        List<UserParticipantDTO> participantDTOS = new ArrayList<>();
        List<User> userList = userDAO.findAll();
        for(User user : userList){
            String status = eventDAO.getUserStatus(eventId,user.getId());
            if(status != null) {
                UserParticipantDTO userParticipantDTO = new UserParticipantDTO();
                userParticipantDTO.setStatus(status);
                userParticipantDTO.setUserId(user.getId());
                userParticipantDTO.setEventId(eventId);
                userParticipantDTO.setUserName(user.getName());
                participantDTOS.add(userParticipantDTO);
            }
        }
        return participantDTOS;
    }

    @Override
    public String changeStatus(int eventId, int userId, String option) {
        int freeSeats;
        switch (option) {
            case "Accept" :
                jdbcTemplate.update(
                        "update event_users set status = ? where pk_event_id = ? and pk_user_id = ?",
                        StatusUserEnum.PARTICIPANT.toString(), eventId,userId);

                Notification notification = new Notification();
                notification.setNotificationTypeEnum(NotificationTypeEnum.REQUEST_EVENT_ACCEPTED);
                Event event = eventDAO.findOne(eventId);
                notification.setUser(event.getUserCreator());
                notification.setDate(new Date());
                notification.setMessage(userDAO.findOne(userId).getName() + " has accepted the invitation for " + event.getTitle());
                notificationDAO.saveAndFlush(notification);

                return "Accept Successfully";
            case "Cancel" :
                jdbcTemplate.update(
                        "delete from event_users where pk_event_id = ? and pk_user_id = ?",
                        eventId,userId);
                return "Cancel Successfully";
            case "Invite" :
                freeSeats = eventDAO.getFreeSeats(eventId);
                if(freeSeats > 0) {
                    jdbcTemplate.update(
                            "insert into event_users values (?,?,?)",
                            eventId, userId, StatusUserEnum.INVITED.toString());
                    return "Invite Successfully";
                }
                return "Invite Fail";
            case "Join" :
                freeSeats = eventDAO.getFreeSeats(eventId);
                if(freeSeats > 0) {
                    jdbcTemplate.update(
                            "insert into event_users values (?,?,?)",
                            eventId, userId, StatusUserEnum.WAITING.toString());
                    return "Join Successfully";
                }
                return "Join Fail";
            case "Decline" :
                jdbcTemplate.update(
                        "delete from event_users where pk_event_id = ? and pk_user_id = ?",
                        eventId,userId);
                return "Decline Successfully";
            case "Interested" :
                jdbcTemplate.update(
                        "insert into event_users values (?,?,?)",
                        eventId, userId, StatusUserEnum.INTERESTED.toString());
                return "Interested Successfully";
            case "Reject" :
                jdbcTemplate.update(
                        "delete from event_users where pk_event_id = ? and pk_user_id = ?",
                        eventId,userId);
                return "Reject Successfully";
            case "Accept Join" :
                freeSeats = eventDAO.getFreeSeats(eventId);
                if(freeSeats > 0) {
                    jdbcTemplate.update(
                            "update event_users set status = ? where pk_event_id = ? and pk_user_id = ?",
                            StatusUserEnum.PARTICIPANT.toString(), eventId,userId);
                    return "Accept Join Request " + userId;
                }
                return "Decline Join Request";
        }
        return "Fail";
    }
}
