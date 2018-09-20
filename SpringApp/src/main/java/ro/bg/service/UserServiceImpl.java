package ro.bg.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ro.bg.dao.*;
import ro.bg.exception.BoardGameServiceException;
import ro.bg.exception.ExceptionMessage;
import ro.bg.model.*;

import java.util.*;

import org.apache.commons.validator.routines.EmailValidator;
import ro.bg.model.constants.NotificationTypeEnum;
import ro.bg.model.dto.NotificationDTO;
import ro.bg.model.dto.UserDTO;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserDAO userDAO;

    @Autowired
    FriendshipDAO friendshipDAO;

    @Autowired
    FriendshipRequestDAO friendshipRequestDAO;

    @Autowired
    NotificationDAO notificationDAO;

    @Autowired
    public JavaMailSender javaMailSender;

    @Override
    public void createUser(User user) throws BoardGameServiceException {
        if(EmailValidator.getInstance().isValid(user.getEmail()) &&
                userDAO.findByEmail(user.getEmail()) == null){
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userDAO.saveAndFlush(user);
        }
        else {
            throw new BoardGameServiceException(ExceptionMessage.USER_ALREADY_EXISTS);
        }
    }

    @Override
    public void updatePersonalInformation(User user) {
        User oldUser = userDAO.findOne(user.getId());
        oldUser.setName(user.getName());
        oldUser.setProfilePicture(user.getProfilePicture());
        oldUser.setTown(user.getTown());
        oldUser.setEvents(user.getEvents());
        oldUser.setBoardGames(user.getBoardGames());
        userDAO.save(oldUser);
    }

    @Override
    public void changePassword(User user) {
        User oldUser = userDAO.findOne(user.getId());
        oldUser.setPassword(user.getPassword());
        userDAO.save(oldUser);
    }

    @Override
    public void deleteUser(User user) {
        userDAO.delete(user.getId());
    }

    @Override
    public User getUser(int idUser) {
        return userDAO.findOne(idUser);
    }

    @Override
    public User getUserInformation(int idUser) throws BoardGameServiceException {
        User user = userDAO.findOne(idUser);
        if(user == null){
            throw new BoardGameServiceException(ExceptionMessage.MISSING_USER);
        }

        List<BoardGame> boardGames = new ArrayList<>(user.getBoardGames());
        for(BoardGame boardGame: boardGames){
            boardGame.setEvents(null);
            boardGame.setUsers(null);
            boardGame.setPubs(null);
            boardGame.setGameReservations(null);
        }

        List<Friendship> friendshipList = new ArrayList<>();
        List<Friendship> friendships = new ArrayList<>(user.getFriendshipsSetOne());
        for(Friendship friendship: friendships){
            if(friendship.getFriendTwo().getId() != user.getId()){
                User user1 = new User();
                user1.setName(friendship.getFriendTwo().getName());
                user1.setId(friendship.getFriendTwo().getId());
                friendshipList.add(new Friendship(user1,null));
            };
        }
        friendships = new ArrayList<>(user.getFriendshipsSetTwo());
        for(Friendship friendship: friendships){
            if(friendship.getFriendOne().getId() != user.getId()){
                User user1 = new User();
                user1.setName(friendship.getFriendOne().getName());
                user1.setId(friendship.getFriendOne().getId());
                // user1.setProfilePicture(friendship.getFriendOne().getProfilePicture());
                friendshipList.add(new Friendship(user1,null));
            };
        }
        user.setBoardGames(new HashSet<BoardGame>(boardGames));
        user.setCreatedEvents(null);
        user.setEventUserSet(null);
        user.setEvents(null);
        user.setFriendshipsSetOne(new HashSet<Friendship>(friendshipList));
        user.setFriendshipsSetTwo(null);
        user.setSenders(null);
        user.setReceivers(null);
        user.setGameReservations(null);
        user.setNotifications(null);
        return user;
    }

    @Override
    public User getUser(Account account) throws BoardGameServiceException {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = userDAO.findByEmail(account.getUsername());
        if(user == null){
            throw new BoardGameServiceException(ExceptionMessage.MISSING_USER);
        }
        else {
            if(!passwordEncoder.matches(account.getPassword(),user.getPassword())){
                throw new BoardGameServiceException(ExceptionMessage.MISSING_USER);
            }
        }

        List<BoardGame> boardGames = new ArrayList<>(user.getBoardGames());
        for(BoardGame boardGame: boardGames){
            boardGame.setEvents(null);
            boardGame.setUsers(null);
            boardGame.setPubs(null);
            boardGame.setGameReservations(null);
        }

        List<Friendship> friendshipList = new ArrayList<>();
        List<Friendship> friendships = new ArrayList<>(user.getFriendshipsSetOne());
        for(Friendship friendship: friendships){
            if(friendship.getFriendTwo().getId() != user.getId()){
                User user1 = new User();
                user1.setName(friendship.getFriendTwo().getName());
                user1.setId(friendship.getFriendTwo().getId());
                friendshipList.add(new Friendship(user1,null));
            };
        }
        friendships = new ArrayList<>(user.getFriendshipsSetTwo());
        for(Friendship friendship: friendships){
            if(friendship.getFriendOne().getId() != user.getId()){
                User user1 = new User();
                user1.setName(friendship.getFriendOne().getName());
                user1.setId(friendship.getFriendOne().getId());
               // user1.setProfilePicture(friendship.getFriendOne().getProfilePicture());
                friendshipList.add(new Friendship(user1,null));
            };
        }
        user.setBoardGames(new HashSet<BoardGame>(boardGames));
        user.setCreatedEvents(null);
        user.setEventUserSet(null);
        user.setEvents(null);
        user.setFriendshipsSetOne(new HashSet<Friendship>(friendshipList));
        user.setFriendshipsSetTwo(null);
        user.setSenders(null);
        user.setReceivers(null);
        user.setGameReservations(null);
        user.setNotifications(null);
        return user;
    }

    @Override
    public void sendPassword(String mail) throws BoardGameServiceException {
        User user = userDAO.findByEmail(mail);
        if(user != null){
            UUID uuid = UUID.randomUUID();
            user.setToken(uuid.toString());
            userDAO.save(user);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(mail);
            message.setFrom("bgboardgame@gmail.com");
            message.setSubject("Reset Password");
            message.setText("Hi,\n\nWe got a request to reset your password\n\nYou receive a token which will help you to insert a new password." +
                    " Your token is: " + uuid.toString() + "\n\nIf you want to change the password, " +
                    "go to login page, click on Forgot Password?. Then click on Insert Token. After you do that complete the blank " +
                    "spaces.\n\nIf you ignore this message, your password will not be change.");
            javaMailSender.send(message);
        }
        else{
            throw new BoardGameServiceException(ExceptionMessage.EMAIL_NOT_EXISTING);
        }
    }

    @Override
    public byte[] getProfileImage(String email) {
        User user = userDAO.findByEmail(email);
        return user.getProfilePicture();
    }

    @Override
    public List<User> getUsersByName(String name) {
        List<User> usersDB = userDAO.findByNameContaining(name);
        List<User> users = new ArrayList<>();
        for(User userDB : usersDB) {
            User user = new User();
            user.setId(userDB.getId());
            user.setName(userDB.getName());
            users.add(user);
        }
        return users;
    }

    @Override
    public UserDTO getSearchUser(int userId,int myId) {
        User user = userDAO.findOne(userId);
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setTown(user.getTown());
        userDTO.setEmail(user.getEmail());
        userDTO.setProfilePicture(user.getProfilePicture());
        Friendship friendship = friendshipDAO.getFriendship(userId,myId);
        if(friendship != null){
            userDTO.setFriend(true);
        }
        else {
            userDTO.setFriend(false);
        }
        if(friendshipRequestDAO.getRequest(userId,myId) != null) {
            userDTO.setRequest(true);
        }
        else {
            userDTO.setRequest(false);
        }
        return userDTO;
    }

    @Override
    public String sendFriendshipRequest(int senderId, int receiverId) {
        FriendshipRequest friendshipRequestDB = friendshipRequestDAO.findRequest(senderId,receiverId);
        if(friendshipRequestDB == null){
            FriendshipRequest friendshipRequest = new FriendshipRequest();
            friendshipRequest.setSender(userDAO.findOne(senderId));
            friendshipRequest.setReceiver(userDAO.findOne(receiverId));
            friendshipRequestDAO.saveAndFlush(friendshipRequest);

            User sender = userDAO.findOne(senderId);
            Notification notification = new Notification();
            notification.setNotificationTypeEnum(NotificationTypeEnum.REQUEST_FRIENDSHIP);
            notification.setUser(userDAO.findOne(receiverId));
            notification.setDate(new Date());
            notification.setFriendshipRequest(friendshipRequest);
            notification.setMessage(sender.getName() + " sent you a friend request");
            notificationDAO.saveAndFlush(notification);
            return "Send request";
        }
        else {
            return "Send already request";
        }
    }

    @Override
    public String deleteFriendship(int senderId, int receiverId) {
        Friendship friendship = friendshipDAO.getFriendship(senderId,receiverId);
        friendshipDAO.delete(friendship);
        return "Request deleted";
    }

    @Override
    public String acceptRequest(int senderId, int receiverId) {
        FriendshipRequest friendshipRequestDB = friendshipRequestDAO.findRequest(senderId,receiverId);
        friendshipRequestDAO.delete(friendshipRequestDB);
        Friendship friendship = new Friendship();
        friendship.setFriendOne(userDAO.findOne(senderId));
        friendship.setFriendTwo(userDAO.findOne(receiverId));
        friendshipDAO.saveAndFlush(friendship);

        User sender = userDAO.findOne(senderId);
        Notification notification = new Notification();
        notification.setNotificationTypeEnum(NotificationTypeEnum.REQUEST_FRIENDSHIP_ACCEPTED);
        notification.setUser(userDAO.findOne(receiverId));
        notification.setDate(new Date());
        notification.setMessage(sender.getName() + " has accepted your friend request");
        notificationDAO.saveAndFlush(notification);
        return "Accept request";
    }

    @Override
    public String deleteRequest(int senderId, int receiverId) {
        FriendshipRequest friendshipRequestDB = friendshipRequestDAO.findRequest(senderId,receiverId);
        friendshipRequestDAO.delete(friendshipRequestDB);
        return "Delete request";
    }

    @Override
    public void updateUser(User user) {
        userDAO.save(user);
    }

    @Override
    public List<NotificationDTO> getAllNotifications(int id) {
        List<Notification> notifications = notificationDAO.getNotifications(id);
        List<NotificationDTO> notificationDTOs = new ArrayList<>();
        for(Notification notification : notifications){
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setId(notification.getId());
            notificationDTO.setMessage(notification.getMessage());
            notificationDTO.setNotificationTypeEnum(notification.getNotificationTypeEnum().toString());
            notificationDTO.setDate(notification.getDate().toString());
            if(notificationDTO.getNotificationTypeEnum().equals("INVITATION_EVENT")){
                notificationDTO.setEventId(notification.getEvent().getId());
            }
            if(notificationDTO.getNotificationTypeEnum().equals("RESERVATION_ACCEPTED")){
                notificationDTO.setEventId(notification.getEvent().getId());
            }
            if(notificationDTO.getNotificationTypeEnum().equals("REQUEST_FRIENDSHIP")){
            }
            if(notificationDTO.getNotificationTypeEnum().equals("REQUEST_FRIENDSHIP_ACCEPTED")){
                int endIndex = notificationDTO.getMessage().indexOf("has accepted your friend request");
                String link = notificationDTO.getMessage().substring(0,endIndex - 1);
                List<User> users = userDAO.findByNameContaining(link);
                if(users.size() != 0){
                    notificationDTO.setUserId(users.get(0).getId());
                }
            }
            if(notificationDTO.getNotificationTypeEnum().equals("REQUEST_EVENT_ACCEPTED")){
                notificationDTO.setEventId(notification.getEvent().getId());
            }
            if(notificationDTO.getNotificationTypeEnum().equals("REQUEST_EVENT_PARTICIPATION")){
                notificationDTO.setEventId(notification.getEvent().getId());
            }
            notificationDTOs.add(notificationDTO);
        }
        return notificationDTOs;
    }

    @Override
    public void resetPassword(String email, String token, String password) throws BoardGameServiceException {
        User user = userDAO.findByEmail(email);
        if(user != null){
            if(token.equals(user.getToken())) {
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                user.setPassword(passwordEncoder.encode(password));
            }
            else {
                throw new BoardGameServiceException(ExceptionMessage.INVALID_TOKEN);
            }
        }
        else{
            throw new BoardGameServiceException(ExceptionMessage.EMAIL_NOT_EXISTING);
        }
    }
}
