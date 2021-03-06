package ro.bg.service;

import ro.bg.exception.BoardGameServiceException;
import ro.bg.model.Account;
import ro.bg.model.User;
import ro.bg.model.dto.NotificationDTO;
import ro.bg.model.dto.UserDTO;

import java.util.List;

public interface UserService {

    void createUser(User user) throws BoardGameServiceException;

    void updatePersonalInformation(User user);

    void changePassword(User user);

    void deleteUser(User user);

    User getUser(int idUser);

    User getUser(Account account) throws BoardGameServiceException;

    User getUserInformation(int idUser) throws BoardGameServiceException;

    void sendPassword(String mail) throws BoardGameServiceException;

    byte[] getProfileImage(String email);

    List<User> getUsersByName(String name);

    UserDTO getSearchUser(int userId, int myId);

    String sendFriendshipRequest(int senderId, int receiverId);

    String deleteFriendship(int senderId, int receiverId);

    String acceptRequest(int senderId, int receiverId);

    String deleteRequest(int senderId, int receiverId);

    void updateUser(User user);

    List<NotificationDTO> getAllNotifications(int id);

    void resetPassword(String email, String token, String password) throws BoardGameServiceException;
}
