package ro.bg.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import ro.bg.dao.*;
import ro.bg.model.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(MockitoJUnitRunner.class)
public class PubServiceImplTest {

    @Mock
    PubDAO pubDAO;

    @Mock
    PubPictureDAO pubPictureDAO;

    @InjectMocks
    PubServiceImpl pubService;

    @Mock
    EventDAO eventDAO;

    @Mock
    UserDAO userDAO;

    @InjectMocks
    EventServiceImpl eventService;

    @Test
    public void testGetPubPicture() {
        PubPicture pubPicture = new PubPicture();
        when(pubPictureDAO.findOne(3)).thenReturn(pubPicture);
        assertEquals(pubPicture,pubService.getPubPicture(3));
        verify(pubPictureDAO, times(1)).findOne(3);
    }

    @Test
    public void testGetPubByEmail() {
        Pub pub = new Pub();
        pub.setEmail("email@yahoo.com");
        pub.setId(3);
        when(pubDAO.findByEmail("email@yahoo.com")).thenReturn(pub);
        assertEquals(pub,pubService.getPubByEmail("email@yahoo.com"));
        verify(pubDAO, times(1)).findByEmail("email@yahoo.com");
    }

    @Test
    public void testUpdatePubInformation() {
        Pub pub1 = new Pub();
        pub1.setEmail("email@yahoo.com");
        pub1.setId(3);
        Pub pub2 = new Pub();
        pub2.setEmail("email@yahoo.com");
        pub2.setId(3);
        pub2.setAddress("address");
        pub2.setDescription("description");
        pub2.setName("name");
        when(pubDAO.findOne(3)).thenReturn(pub1);
        pubService.updatePubInformation(pub2);
        verify(pubDAO, times(1)).findOne(3);

        assertEquals("address",pub1.getAddress());
        assertEquals("name",pub1.getName());
        assertEquals("description",pub1.getDescription());
    }

    @Test
    public void testGetPubs() {
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
    public void testDeletePub() {
        Event event = new Event();
        event.setId(3);
        eventService.deleteEvent(event);
        verify(eventDAO, times(1)).delete(3);
    }
}
