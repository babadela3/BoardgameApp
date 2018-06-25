package ro.bg.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ro.bg.model.Event;
import ro.bg.model.EventUser;
import ro.bg.model.Pub;
import ro.bg.model.User;
import ro.bg.model.dto.BoardGameAndroidDTO;
import ro.bg.model.dto.EventDTO;
import ro.bg.model.dto.FriendAndroidDTO;
import ro.bg.service.EventService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Controller
public class EventController {

    @Autowired
    EventService eventService;

    @RequestMapping(value = "/createEvent", method = RequestMethod.POST)
    public ResponseEntity<Object> createEvent(@ModelAttribute("name") String name,
                                              @ModelAttribute("description") String description,
                                              @ModelAttribute("numberPlayers") String numberPlayers,
                                              @ModelAttribute("friends") String friends,
                                              @ModelAttribute("games") String games,
                                              @ModelAttribute("date") String dateString,
                                              @ModelAttribute("pubId") String pubId,
                                              @ModelAttribute("address") String address,
                                              @ModelAttribute("latitude") String latitudeString,
                                              @ModelAttribute("longitude") String longitudeString,
                                              @ModelAttribute("creatorId") String creatorId) {

        Gson gson = new Gson();
        ArrayList<FriendAndroidDTO> friendsDTO = gson.fromJson(friends, new TypeToken<List<FriendAndroidDTO>>(){}.getType());
        ArrayList<BoardGameAndroidDTO> gamesDTO = gson.fromJson(games, new TypeToken<List<BoardGameAndroidDTO>>(){}.getType());
        User creator = new User();
        creator.setId(Integer.parseInt(creatorId));
        Pub pub = new Pub();
        if(!pubId.equals("")){
            pub.setId(Integer.parseInt(pubId));
        }
        else {
            pub = null;
        }
        double latitude = Double.parseDouble(latitudeString);
        double longitude = Double.parseDouble(longitudeString);
        int players = Integer.parseInt(numberPlayers);
        DateFormat format = new SimpleDateFormat("HH:mm dd.MM.yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        eventService.createEvent(name,description,players,friendsDTO,gamesDTO,date,pub,address,latitude,longitude,creator);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @RequestMapping(value = "/getUserEvents", method = RequestMethod.POST)
    public ResponseEntity<Object> getEventsByUser(@ModelAttribute("userId") String userId) {
        List<EventDTO> eventDTOS = eventService.getEventsByUserId(Integer.parseInt(userId));
        return ResponseEntity.status(HttpStatus.OK).body(eventDTOS);
    }

    @RequestMapping(value = "/getEvent", method = RequestMethod.POST)
    public ResponseEntity<Object> getEvent(@ModelAttribute("eventId") String eventId) {
        EventDTO eventDTO = eventService.getEvent(Integer.parseInt(eventId));
        return ResponseEntity.status(HttpStatus.OK).body(eventDTO);
    }

    @RequestMapping(value = "/changeUserStatus", method = RequestMethod.POST)
    public ResponseEntity<Object> changeUserStatus(@ModelAttribute("eventId") String eventId,
                                                   @ModelAttribute("userId") String userId,
                                                   @ModelAttribute("option") String option) {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.changeStatus(Integer.parseInt(eventId),
                Integer.parseInt(userId),option));
    }

    @RequestMapping(value = "/getUsersStatus", method = RequestMethod.POST)
    public ResponseEntity<Object> getUsersStatus(@ModelAttribute("eventId") String eventId) {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getUsers(Integer.parseInt(eventId)));
    }

    @RequestMapping(value = "/updateEvent", method = RequestMethod.POST)
    public String updateEvent(@RequestBody Event event) {
        eventService.updateEventInformation(event);
        return "";
    }

    @RequestMapping(value = "/deleteEvent", method = RequestMethod.POST)
    public String deleteEvent(@RequestBody Event event) {
        eventService.deleteEvent(event);
        return "";
    }

    @RequestMapping(value = "/allEvents", method = RequestMethod.POST)
    public ResponseEntity<Object> getAllEvents(){
        List<Event> events = eventService.getAllEvents();
        return ResponseEntity.status(HttpStatus.OK).body(events);
    }

    @RequestMapping(value = "/getEventsByName", method = RequestMethod.POST)
    public ResponseEntity<Object> getEventsByName(@ModelAttribute("name") String name){
        List<EventDTO> events = eventService.getEventsByName(name);
        return ResponseEntity.status(HttpStatus.OK).body(events);
    }

    @RequestMapping(value = "/events", method = RequestMethod.GET)
    public String getEvents(){
        List<Event> events = eventService.getCurrentEvents();
        System.out.println(events);
        return "";
    }

    @RequestMapping(value = "/freeSeats", method = RequestMethod.POST)
    public String getFreeSeats(@RequestBody Event event) {
        System.out.println(eventService.getFreeSeats(event));
        return "";
    }

}
