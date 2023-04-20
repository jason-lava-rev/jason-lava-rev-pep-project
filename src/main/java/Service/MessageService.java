package Service;
import java.util.List;
import Model.Message;
import DAO.MessageDAO;

public class MessageService {
    MessageDAO messageDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
    }

    // assign messageDAO
    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    // Get all messages
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

     // get all messages by user
    public List<Message> getAllMessagesByAccountId(int id) {
        return messageDAO.getAllMessagesByAccountId(id);
    }

    // get message by id
    public Message getMessageById(int id) {
        return messageDAO.getMessageById(id);
    }

    // add new message
    public Message addMessage(Message message) {
        int messageLength = message.getMessage_text().length();
        if (messageLength >= 255 || messageLength == 0) {
            return null;
        }
        return messageDAO.insertMessage(message);
    }

    // delete message byid
    public Message deleteMessage(int id) {
        Message message = messageDAO.getMessageById(id);
        if (message == null) {
            return null;
        }
        boolean deleted = messageDAO.deleteMessage(id);
        if (deleted) {
            return message;
        }
        return null;
    }

    // find and update message
    public Message updateMessage(int id, String messageText) {
        if (messageText.length() >= 255 || messageText.length() == 0) {
            return null;
        }
        Message found = messageDAO.getMessageById(id);
        if (found == null) {
            return null;
        }
        return messageDAO.updateMessage(id, messageText);
    }
}