package ro.bg.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import ro.bg.dao.EventDAO;
import ro.bg.dao.UserDAO;
import ro.bg.model.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(MockitoJUnitRunner.class)
public class EventServiceImplTest {

    @Mock
    EventDAO eventDAO;

    @Mock
    UserDAO userDAO;

    @InjectMocks
    EventServiceImpl eventService;

    @Test
    public void testGetCurrentEvents() {
        List<Event> eventList = new ArrayList<>();
        Event event1 = new Event();
        Event event2 = new Event();
        eventList.add(event1);
        eventList.add(event2);
        Mockito.when(eventDAO.findCurrentEvents()).thenReturn(eventList);
        assertEquals(eventList, eventService.getCurrentEvents());
        verify(eventDAO, times(1)).findCurrentEvents();
    }

    @Test
    public void testGetAllEvents() {
        User user = new User();
        user.setId(3);
        user.setName("Name");
        List<Event> eventList = new ArrayList<>();
        Event event1 = new Event();
        event1.setDate(new Date());
        event1.setUserCreator(user);
        event1.setBoardGames(new HashSet<>(new ArrayList<BoardGame>()));
        Event event2 = new Event();
        event2.setDate(new Date());
        event2.setUserCreator(user);
        event2.setBoardGames(new HashSet<>(new ArrayList<BoardGame>()));
        eventList.add(event1);
        eventList.add(event2);


        Mockito.when(eventDAO.findCurrentEvents()).thenReturn(eventList);
        assertEquals(eventList, eventService.getAllEvents());
        verify(eventDAO, times(1)).findCurrentEvents();
    }

    @Test
    public void testDeleteEvent() {
        Event event = new Event();
        event.setId(3);
        eventService.deleteEvent(event);
        verify(eventDAO, times(1)).delete(3);
    }

    @Test
    public void testGetFreeSeats() {
        Event event = new Event();
        event.setId(3);
        Mockito.when(eventDAO.getFreeSeats(3)).thenReturn(4);
        assertEquals(4,eventService.getFreeSeats(event));
        verify(eventDAO, times(1)).getFreeSeats(3);
    }

    @Test
    public void testChangeUserStatus() {
        Event event = new Event();
        event.setId(3);
        User user = new User();
        user.setId(3);
        EventUser eventUser = new EventUser();
        EventUserId eventUserId = new EventUserId();
        eventUserId.setEvent(event);
        eventUserId.setUser(user);
        eventUser.setPk(eventUserId);
        event.setEventUserSet(new HashSet<>(new ArrayList<EventUser>()));
        Mockito.when(eventDAO.findOne(3)).thenReturn(event);
        eventService.changeUserStatus(eventUser);
        verify(eventDAO, times(1)).findOne(3);
        verify(userDAO, times(1)).findOne(3);
        verify(eventDAO, times(1)).save(event);
    }
}
