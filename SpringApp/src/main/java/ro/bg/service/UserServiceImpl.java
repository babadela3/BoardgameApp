package ro.bg.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ro.bg.dao.UserDAO;
import ro.bg.exception.BoardGameServiceException;
import ro.bg.exception.ExceptionMessage;
import ro.bg.model.Account;
import ro.bg.model.User;

import java.util.List;
import org.apache.commons.validator.routines.EmailValidator;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserDAO userDAO;

    @Autowired
    public JavaMailSender javaMailSender;

    @Override
    public void createUser(User user) throws BoardGameServiceException {
        if(EmailValidator.getInstance().isValid(user.getEmail()) && userDAO.findByEmail(user.getEmail()) == null){
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
    public User getUser(Account account) throws BoardGameServiceException {
        User user = userDAO.findByEmailAndPassword(account.getUsername(),account.getPassword());
        if(user == null){
            throw new BoardGameServiceException(ExceptionMessage.MISSING_USER);
        }
        user.setBoardGames(null);
        user.setCreatedEvents(null);
        user.setEventUserSet(null);
        user.setEvents(null);
        user.setFriendshipsSetOne(null);
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
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(mail);
            message.setFrom("bgboardgame@gmail.com");
            message.setSubject("BoardGame Password");
            message.setText("Hi,\n\nWe got a request to send your BoardGame password.\n\nYour password is: " + user.getPassword() +
                    " .\n\nIf you ignore this message, your password will not be change.");
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
}
