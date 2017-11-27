package ro.bg.service;

import ro.bg.model.Friendship;
import ro.bg.model.Message;

import java.util.List;

public interface MessageService {

    void sendMessage(Message message);

    List<Message> getAllMessages(Friendship friendship);
}
