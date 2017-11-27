package ro.bg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ro.bg.model.Friendship;
import ro.bg.model.FriendshipRequest;
import ro.bg.model.User;
import ro.bg.service.FriendshipService;

import java.util.List;

@Controller
public class FriendshipController {

    @Autowired
    FriendshipService friendshipService;

    @RequestMapping(value = "/createFriendship", method = RequestMethod.POST)
    public String createFriendship(@RequestBody FriendshipRequest friendship) {
        friendshipService.insertFriendship(friendship);
        return "";
    }

    @RequestMapping(value = "/deleteFriendship", method = RequestMethod.POST)
    public String deleteFriendship(@RequestBody Friendship friendship) {
        friendshipService.deleteFriendship(friendship);
        return "";
    }

    @RequestMapping(value = "/getFriends", method = RequestMethod.POST)
    public String getAllFriends(@RequestBody User user) {
        List<User> friends = friendshipService.getAllFriends(user);
        return "";
    }
}
