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

    public Message getMessageById(String messageId){
        return messageDAO.getMessageById(Integer.parseInt(messageId));
    }

    public Message deleteMessageById(String messageId){
        Message message = getMessageById(messageId);
        if(message != null){
            messageDAO.deleteMessageById(Integer.parseInt(messageId));
            return message;
        } else {
            return null;
        }
    }

    public Message updateMessageById(String messageId, Message message){
        messageDAO.updateMessageById(Integer.parseInt(messageId), message);
        Message updatMessage = messageDAO.getMessageById(Integer.parseInt(messageId));
        if(updatMessage.getMessage_text().equals(message.getMessage_text())){
            return updatMessage;
        } 
        return null;
    }
}
