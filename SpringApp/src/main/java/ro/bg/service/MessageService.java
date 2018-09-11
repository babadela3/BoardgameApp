package ro.bg.service;

import ro.bg.model.Friendship;
import ro.bg.model.Message;
import ro.bg.model.dto.ChatMessageDTO;

import java.util.List;

public interface MessageService {

    void sendMessage(Message message);

    List<ChatMessageDTO> getAllMessages(Friendship friendship);
}
