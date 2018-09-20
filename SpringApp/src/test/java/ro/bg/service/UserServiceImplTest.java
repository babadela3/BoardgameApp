package ro.bg.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import ro.bg.dao.FriendshipDAO;
import ro.bg.dao.FriendshipRequestDAO;
import ro.bg.dao.UserDAO;
import ro.bg.exception.BoardGameServiceException;
import ro.bg.model.*;
import ro.bg.model.dto.UserDTO;

import java.util.ArrayList;
import java.util.HashSet;
import   java.util.List;

import static junit.framework.Assert.assertNull;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @Mock
    UserDAO userDAO;

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    FriendshipRequestDAO friendshipRequestDAO;

    @Mock
    FriendshipDAO friendshipDAO;

    @Test
    public void testGetUser() {
        User user = new User();
        user.setId(3);
        Mockito.when(userDAO.findOne(3)).thenReturn(user);
        assertEquals(user, userService.getUser(3));
        verify(userDAO, times(1)).findOne(3);
    }

    @Test
    public void testUpdatePersonalInformation() {
        User user = new User();
        user.setId(3);
        user.setName("Nume");
        user.setTown("Bucuresti");
        User oldUser = new User();
        oldUser.setId(3);
        Mockito.when(userDAO.findOne(3)).thenReturn(oldUser);
        userService.updatePersonalInformation(user);
        verify(userDAO, times(1)).findOne(3);
        verify(userDAO, times(1)).save(oldUser);
        assertEquals("Nume", oldUser.getName());
        assertEquals("Bucuresti",oldUser.getTown());
    }

    @Test
    public void testDeleteUser() {
        User user = new User();
        user.setId(2);
        userService.deleteUser(user);
        verify(userDAO, times(1)).delete(2);
    }

    @Test
    public void testGetInformation() throws BoardGameServiceException {
        User user = new User();
        user.setId(2);
        user.setName("Nume");
        user.setEvents(new HashSet<Event>(new ArrayList<>()));
        user.setBoardGames(new HashSet<BoardGame>(new ArrayList<>()));
        user.setFriendshipsSetOne(new HashSet<>(new ArrayList<>()));
        user.setFriendshipsSetTwo(new HashSet<>(new ArrayList<>()));
        Mockito.when(userDAO.findOne(2)).thenReturn(user);
        userService.getUserInformation(2);
        verify(userDAO, times(1)).findOne(2);
        assertEquals("Nume",user.getName());
        assertNotNull(user.getBoardGames());
        assertNull(user.getEvents());
    }

    @Test
    public void testUsersByName() {
        List<User> userList = new ArrayList<>();
        User user1 = new User();
        user1.setName("ANume");
        User user2 = new User();
        user2.setName("TNumea");
        userList.add(user1);
        userList.add(user2);
        Mockito.when(userDAO.findByNameContaining("Nume")).thenReturn(userList);
        assertEquals(userList,userService.getUsersByName("Nume"));
        verify(userDAO, times(1)).findByNameContaining("Nume");
    }

    @Test
    public void testGetSearchUser() {
        User user = new User();
        user.setId(3);
        user.setTown("town");
        user.setEmail("EMAIL");
        UserDTO userDTO = new UserDTO();
        Mockito.when(userDAO.findOne(3)).thenReturn(user);
        Mockito.when(friendshipDAO.getFriendship(3,2)).thenReturn(null);
        Mockito.when(friendshipRequestDAO.getRequest(3,2)).thenReturn(new FriendshipRequest());
        userService.getSearchUser(3,2);
        verify(userDAO, times(1)).findOne(3);
        verify(friendshipDAO, times(1)).getFriendship(3,2);
    }

    @Test
    public void testSendFriendshipRequest() {
        Mockito.when(friendshipRequestDAO.findRequest(3,2)).thenReturn(new FriendshipRequest());
        assertEquals("Send already request",userService.sendFriendshipRequest(3,2));
        verify(friendshipRequestDAO, times(1)).findRequest(3,2);
    }

    @Test
    public void testDeleteFriendship() {
        Friendship friendship = new Friendship();
        Mockito.when(friendshipDAO.getFriendship(3,2)).thenReturn(friendship);
        assertEquals("Request deleted",userService.deleteFriendship(3,2));
        verify(friendshipDAO, times(1)).getFriendship(3,2);
        verify(friendshipDAO, times(1)).delete(friendship);
    }

    @Test
    public void testDeleteRequest() {
        FriendshipRequest friendship = new FriendshipRequest();
        Mockito.when(friendshipRequestDAO.findRequest(3,2)).thenReturn(friendship);
        assertEquals("Delete request",userService.deleteRequest(3,2));
        verify(friendshipRequestDAO, times(1)).findRequest(3,2);
        verify(friendshipRequestDAO, times(1)).delete(friendship);
    }
}
