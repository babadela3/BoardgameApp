package ro.bg.service;


import ro.bg.model.Friendship;
import ro.bg.model.FriendshipRequest;
import ro.bg.model.User;

import java.util.List;

public interface FriendshipService {

    List<User> getAllFriends(User user);

    void insertFriendship(FriendshipRequest friendship);

    void deleteFriendship(Friendship friendship);
}
