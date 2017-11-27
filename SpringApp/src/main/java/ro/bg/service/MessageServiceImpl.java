package ro.bg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.bg.dao.MessageDAO;
import ro.bg.model.Friendship;
import ro.bg.model.Message;

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
    public List<Message> getAllMessages(Friendship friendship) {
        return messageDAO.getMessages(friendship.getId());
    }
}
