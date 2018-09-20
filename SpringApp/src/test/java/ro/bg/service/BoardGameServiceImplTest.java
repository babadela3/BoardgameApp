package ro.bg.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import ro.bg.dao.BoardGameDAO;
import ro.bg.dao.EventDAO;
import ro.bg.dao.UserDAO;
import ro.bg.model.BoardGame;
import ro.bg.model.Event;
import ro.bg.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(MockitoJUnitRunner.class)
public class BoardGameServiceImplTest {

    @Mock
    BoardGameDAO boardGameDAO;

    @InjectMocks
    BoardGameServiceImpl boardGameService;

    @Mock
    EventDAO eventDAO;

    @Mock
    UserDAO userDAO;

    @InjectMocks
    EventServiceImpl eventService;

    @Test
    public void testGetAllById() {
        List<BoardGame> boardGames = new ArrayList<>();
        BoardGame boardGame1 = new BoardGame();
        boardGame1.setId(3);
        boardGame1.setName("Joc");
        BoardGame boardGame2 = new BoardGame();
        boardGame2.setId(3);
        boardGame2.setName("ZameJoc");
        boardGames.add(boardGame1);
        boardGames.add(boardGame2);

        when(boardGameDAO.getAllById(3)).thenReturn(boardGames);
        assertEquals(boardGames,boardGameService.getAllById(3));
        verify(boardGameDAO, times(1)).getAllById(3);
    }

    @Test
    public void testFindById() {
        BoardGame boardGame1 = new BoardGame();
        boardGame1.setId(3);
        boardGame1.setName("Joc");
        when(boardGameDAO.findOne(3)).thenReturn(boardGame1);
        assertEquals(boardGame1,boardGameService.findById(3));
        verify(boardGameDAO, times(1)).findOne(3);
    }

    @Test
    public void testGetBoardGames() {
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
    public void testDeleteBoardGame() {
        Event event = new Event();
        event.setId(3);
        eventService.deleteEvent(event);
        verify(eventDAO, times(1)).delete(3);
    }
}
