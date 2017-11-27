package ro.bg.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.bg.dao.UserDAO;
import ro.bg.model.User;

import java.util.List;
import org.apache.commons.validator.routines.EmailValidator;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserDAO userDAO;

    @Override
    public void createUser(User user) {
        if(EmailValidator.getInstance().isValid(user.getEmail()) && userDAO.findByEmail(user.getEmail()) == null){
            userDAO.saveAndFlush(user);
        };
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
}
