package ro.bg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ro.bg.model.Friendship;
import ro.bg.model.Message;
import ro.bg.model.User;
import ro.bg.model.dto.ChatMessageDTO;
import ro.bg.service.FriendshipService;
import ro.bg.service.MessageService;

import java.util.Date;
import java.util.List;

@Controller
public class MessageController {

    @Autowired
    MessageService messageService;

    @Autowired
    FriendshipService friendshipService;

    @RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
    public ResponseEntity<Object> sendMessage(@ModelAttribute("sender") String senderId,
                                              @ModelAttribute("receiver") String receiverId,
                                              @ModelAttribute("message") String message) {

        Friendship friendship = friendshipService.getFriendShip(Integer.parseInt(senderId),Integer.parseInt(receiverId));
        Date date = new Date();
        messageService.sendMessage(new Message(friendship,Integer.parseInt(senderId),Integer.parseInt(receiverId),message,date));
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @RequestMapping(value = "/allMessages", method = RequestMethod.POST)
    public ResponseEntity<Object> getMessages(@ModelAttribute("friendOne") String friendOne,
                                              @ModelAttribute("friendTwo") String friendTwo) {
        Friendship friendship = friendshipService.getFriendShip(Integer.parseInt(friendOne),Integer.parseInt(friendTwo));
        List<ChatMessageDTO> messageList = messageService.getAllMessages(friendship);
        return ResponseEntity.status(HttpStatus.OK).body(messageList);
    }
}
