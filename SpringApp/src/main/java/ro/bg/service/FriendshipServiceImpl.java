package ro.bg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.bg.dao.FriendshipDAO;
import ro.bg.dao.FriendshipRequestDAO;
import ro.bg.dao.UserDAO;
import ro.bg.model.Friendship;
import ro.bg.model.FriendshipRequest;
import ro.bg.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class FriendshipServiceImpl implements FriendshipService {

    @Autowired
    FriendshipDAO friendshipDAO;

    @Autowired
    FriendshipRequestDAO friendshipRequestDAO;

    @Autowired
    UserDAO userDAO;

    @Override
    public List<User> getAllFriends(User user) {
        List<Friendship> friendships = friendshipDAO.findAll();
        List<User> friends = new ArrayList<User>();
        for(Friendship friendship : friendships){
            if(friendship.getFriendOne().getId() == user.getId()){
                friends.add(friendship.getFriendTwo());
            }
            if(friendship.getFriendTwo().getId() == user.getId()){
                friends.add(friendship.getFriendOne());
            }
        }
        Collections.sort(friends,new Comparator<User>() {
            @Override
            public int compare(User user1, User user2) {
                return user1.getName().compareTo(user2.getName());
            }
        });
        return friends;
    }

    @Override
    public void insertFriendship(FriendshipRequest friendship) {
        Friendship friendship1 = new Friendship();
        friendship1.setFriendOne(friendship.getSender());
        friendship1.setFriendTwo(friendship.getReceiver());
        friendshipRequestDAO.delete(friendship.getId());
        friendshipDAO.saveAndFlush(friendship1);
    }

    @Override
    public void deleteFriendship(Friendship friendship) {
        List<Friendship> friendships = friendshipDAO.findAll();
        for(Friendship friendship1 : friendships) {
            if((friendship1.getFriendOne().getId() == friendship.getFriendOne().getId() && friendship1.getFriendTwo().getId() == friendship.getFriendTwo().getId()) ||
                    (friendship1.getFriendTwo().getId() == friendship.getFriendOne().getId() && friendship1.getFriendOne().getId() == friendship.getFriendTwo().getId())){
                friendshipDAO.delete(friendship1.getId());
                break;
            }
        }
    }

    @Override
    public Friendship getFriendShip(int friendOne, int friendTwo) {
        return friendshipDAO.getFriendship(friendOne,friendTwo);
    }
}
