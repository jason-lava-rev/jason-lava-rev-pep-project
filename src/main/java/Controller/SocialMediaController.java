package Controller;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        accountService = new AccountService();
        messageService = new MessageService();
    }
    
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        app.post("/register", this::registerNewAccountHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::postNewMessageHandler);
        
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageHandler);
        
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        
        app.delete("/messages/{message_id}", this::deleteMessageByIDHandler);
        
        app.get("/accounts/{account_id}/messages", this::getMessagesByAccountHandler);

        return app;
    }



    // Register new accounts handler
    private void registerNewAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account newAccount = accountService.addAccount(account);
        if (newAccount == null) {
            ctx.status(400);
        } else {
            ctx.json(mapper.writeValueAsString(newAccount));
        }
    }

     // Login handler
    private void loginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account credentials = mapper.readValue(ctx.body(), Account.class);
        Account verified = accountService.verifyCredentials(credentials);
        if (verified == null) {
            ctx.status(401);
        } else {
            ctx.json(mapper.writeValueAsString(verified));
        }
    }

    // Message handler
    private void getAllMessagesHandler(Context ctx) {
        ctx.json(messageService.getAllMessages());
    }

    // Post new message handler
    private void postNewMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message addMessage = messageService.addMessage(message);
        if (addMessage == null) {
            ctx.status(400);
        } else {
            ctx.json(mapper.writeValueAsString(addMessage));
        }
    }

    // get message by id handler
    public void getMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(id);
        if (message == null) {
            ctx.json("");
        } else {
            ctx.json(mapper.writeValueAsString(message));
        }
    }

    // update the message handler
    public void updateMessageHandler(Context ctx) throws JsonProcessingException {
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message updated = messageService.updateMessage(id, message.getMessage_text());
        if (updated == null) {
            ctx.status(400);
        } else {
            ctx.json(mapper.writeValueAsString(updated));
        }
    }

    // Delete message by id handler
    public void deleteMessageByIDHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message deleted = messageService.deleteMessage(id);
        if (deleted == null) {
            ctx.json("");
        } else {
            ctx.json(mapper.writeValueAsString(deleted));
        }
    }

    // get all message from account handler
    public void getMessagesByAccountHandler(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("account_id"));
        ctx.json(messageService.getAllMessagesByAccountId(id));
    }
}