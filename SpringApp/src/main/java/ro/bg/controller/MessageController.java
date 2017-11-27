package ro.bg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ro.bg.model.Friendship;
import ro.bg.model.Message;
import ro.bg.service.MessageService;

import java.util.List;

@Controller
public class MessageController {

    @Autowired
    MessageService messageService;

    @RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
    public String sendMessage(@RequestBody Message message) {
        messageService.sendMessage(message);
        return "";
    }

    @RequestMapping(value = "/allMessages", method = RequestMethod.POST)
    public String getMessages(@RequestBody Friendship friendship) {
        List<Message> messageList = messageService.getAllMessages(friendship);
        for(Message message : messageList){
            System.out.println(message);
        }
        return "";
    }
}
