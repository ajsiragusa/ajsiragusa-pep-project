package Service;

import DAO.MessageDAO;
import Model.Message;
import java.util.List;

public class MessageService {
    MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }

    public Message createMessage(Message message)
    {
        return messageDAO.createMessage(message);
    }

    public List<Message> getAllMessages()
    {
        return messageDAO.getAllMessages();
    }
    
    public Message getMessageById(String id)
    {
        return messageDAO.getMessageByID(id);
    }

    public Message deleteMessageByID(String id)
    {
        Message message = messageDAO.getMessageByID(id);
        if(message != null)
        {
            messageDAO.deleteMessageByID(id);
            return message;
        }
        return null;   
    }

    public Message patchMessageByID(String id, String body)
    {
        Message message = messageDAO.getMessageByID(id);
        if(message != null && body.length() <= 255 && !body.isEmpty())
        {
            messageDAO.patchMessageByID(id, body);
            return messageDAO.getMessageByID(id);
        }
        return null;
    }

    public List<Message> getMessagesByUser(int id)
    {
        return messageDAO.getMessagesByUser(id);
    }
}
