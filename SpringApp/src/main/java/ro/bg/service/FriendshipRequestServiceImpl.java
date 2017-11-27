package ro.bg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.bg.dao.FriendshipRequestDAO;
import ro.bg.dao.UserDAO;
import ro.bg.model.FriendshipRequest;
import ro.bg.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class FriendshipRequestServiceImpl implements FriendshipRequestService{

    @Autowired
    FriendshipRequestDAO friendshipRequestDAO;

    @Autowired
    UserDAO userDAO;

    @Override
    public List<User> getAllFriendshipRequests(User user) {
        List<FriendshipRequest> friendshipRequests = friendshipRequestDAO.findReceiveRequests(user.getId());
        List<User> requests = new ArrayList<User>();
        for(FriendshipRequest friendshipRequest : friendshipRequests){
            requests.add(friendshipRequest.getSender());
        }
        return requests;
    }

    @Override
    public void insertFriendshipRequest(FriendshipRequest friendshipRequest) {
        FriendshipRequest friendshipRequest1 = new FriendshipRequest();
        friendshipRequest1.setSender(userDAO.findOne(friendshipRequest.getSender().getId()));
        friendshipRequest1.setReceiver(userDAO.findOne(friendshipRequest.getReceiver().getId()));
        friendshipRequestDAO.saveAndFlush(friendshipRequest1);
    }

    @Override
    public void deleteFriendshipRequest(FriendshipRequest friendshipRequest) {
        List<FriendshipRequest> requests = friendshipRequestDAO.findReceiveRequests(friendshipRequest.getSender().getId());
        for(FriendshipRequest friendshipRequest1 : requests){
            if(friendshipRequest1.getReceiver().getId() == friendshipRequest.getReceiver().getId()){
                friendshipRequestDAO.delete(friendshipRequest.getId());
                break;
            }
        }
    }
}
