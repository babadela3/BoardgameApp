package ro.bg.service;

import ro.bg.model.FriendshipRequest;
import ro.bg.model.User;

import java.util.List;

public interface FriendshipRequestService {

    List<User> getAllFriendshipRequests(User user);

    void insertFriendshipRequest(FriendshipRequest friendshipRequest);

    void deleteFriendshipRequest(FriendshipRequest friendshipRequest);
}
