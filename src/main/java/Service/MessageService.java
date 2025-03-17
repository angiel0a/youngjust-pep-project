package Service;

import DAO.MessageDAO;
import Model.Message;
import java.util.List;

public class MessageService {
    public MessageDAO messageDAO;

    public MessageService(){
        this.messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    public Message addMessage(Message message){
        return messageDAO.insertMessage(message);
    }

    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int messageId){
        return messageDAO.getMessageById(messageId);
    }
}
