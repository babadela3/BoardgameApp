package ro.bg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ro.bg.model.Friendship;
import ro.bg.model.FriendshipRequest;
import ro.bg.model.User;
import ro.bg.service.FriendshipRequestService;

import java.util.List;

@Controller
public class FriendshipRequestController {

    @Autowired
    FriendshipRequestService friendshipRequestService;

    @RequestMapping(value = "/createFriendshipRequest", method = RequestMethod.POST)
    public String createFriendshipRequest(@RequestBody FriendshipRequest friendship) {
        friendshipRequestService.insertFriendshipRequest(friendship);
        return "";
    }

    @RequestMapping(value = "/deleteFriendshipRequest", method = RequestMethod.POST)
    public String deleteFriendshipRequest(@RequestBody FriendshipRequest friendship) {
        friendshipRequestService.deleteFriendshipRequest(friendship);
        return "";
    }

    @RequestMapping(value = "/getFriendRequests", method = RequestMethod.POST)
    public String getAllFriendRequests(@RequestBody User user) {
        List<User> friendRequests = friendshipRequestService.getAllFriendshipRequests(user);
        return "";
    }
}
