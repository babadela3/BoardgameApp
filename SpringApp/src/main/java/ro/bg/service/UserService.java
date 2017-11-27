package ro.bg.service;

import ro.bg.model.User;

public interface UserService {

    void createUser(User user);

    void updatePersonalInformation(User user);

    void changePassword(User user);

    void deleteUser(User user);
}
