package ro.bg.service;

import ro.bg.exception.BoardGameServiceException;
import ro.bg.model.Account;
import ro.bg.model.User;

public interface UserService {

    void createUser(User user) throws BoardGameServiceException;

    void updatePersonalInformation(User user);

    void changePassword(User user);

    void deleteUser(User user);

    User getUser(Account account) throws BoardGameServiceException;

    void sendPassword(String mail) throws BoardGameServiceException;

    byte[] getProfileImage(String email);
}
