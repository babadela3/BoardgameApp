package ro.bg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.bg.dao.MessageDAO;
import ro.bg.model.Friendship;
import ro.bg.model.Message;
import ro.bg.model.dto.ChatMessageDTO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService{

    @Autowired
    MessageDAO messageDAO;

    @Override
    public void sendMessage(Message message) {
        messageDAO.saveAndFlush(message);
    }

    @Override
    public List<ChatMessageDTO> getAllMessages(Friendship friendship) {
        List<ChatMessageDTO> messages = new ArrayList<>();
        List<Message> messageList = messageDAO.getMessages(friendship.getId());
        for(Message message : messageList){
            ChatMessageDTO chatMessage = new ChatMessageDTO();
            chatMessage.setMessage(message.getMessage());
            chatMessage.setReceiver(message.getReceiver());
            chatMessage.setSender(message.getSender());

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(message.getDate());
            chatMessage.setDate(String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
            messages.add(chatMessage);
        }
        return messages;
    }
}
