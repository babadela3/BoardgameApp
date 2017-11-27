package ro.bg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ro.bg.model.Event;
import ro.bg.model.EventUser;
import ro.bg.service.EventService;

import java.util.List;

@Controller
public class EventController {

    @Autowired
    EventService eventService;

    @RequestMapping(value = "/createEvent", method = RequestMethod.POST)
    public String createEvent(@RequestBody Event event) {
        System.out.println(event);
        eventService.createEvent(event);
        return "";
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

    @RequestMapping(value = "/allEvents", method = RequestMethod.GET)
    public String getAllEvents(){
        List<Event> events = eventService.getAllEvents();
        System.out.println(events);
        return "";
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

    @RequestMapping(value = "/changeUserStatus", method = RequestMethod.POST)
    public String changeUserStatus(@RequestBody EventUser eventUser) {
        eventService.changeUserStatus(eventUser);
        return "";
    }
}
