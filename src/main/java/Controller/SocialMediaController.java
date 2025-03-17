package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    AccountService accountService; 
    MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postRegistrationHandler);
        app.post("/login", this::postLoginHandler);
        app.post("/messages", this::postMessagesHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException 
     * @throws JsonMappingException 
     */
    private void postRegistrationHandler(Context ctx) throws JsonMappingException, JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        Account account = om.readValue(ctx.body(), Account.class);
        if(!account.getUsername().isEmpty() 
        && account.getPassword().length() >= 4 
        && !accountService.accountExists(account.getUsername())){
            Account accountCreated = accountService.addAccount(account);
            if(accountCreated!=null){
                ctx.json(om.writeValueAsString(accountCreated));
                ctx.status(200);
            }
        } else {
            ctx.status(400);
        } 
    }

    private void postLoginHandler(Context ctx) throws JsonMappingException, JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        Account account = om.readValue(ctx.body(), Account.class);
        if(accountService.getLoggedAccount(account) != null){
            Account loggedAccount = accountService.getLoggedAccount(account);
            ctx.json(om.writeValueAsString(loggedAccount));
            ctx.status(200);
        } else {
            ctx.status(401);
        }
    }

    private void postMessagesHandler(Context ctx) throws JsonMappingException, JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        Message message = om.readValue(ctx.body(), Message.class);
        if(!message.getMessage_text().isEmpty() 
        && message.getMessage_text().length() <= 255
        && accountService.isValidPoster(message.getPosted_by())){
            Message messageCreated = messageService.addMessage(message);
            if(messageCreated != null){
                ctx.json(om.writeValueAsString(messageCreated));
                ctx.status(200);
            }
        } else {
            ctx.status(400);
        }
    }

    private void getAllMessagesHandler(Context ctx){
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
        ctx.status(200);
    }

    private void getMessageByIdHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        Message message = messageService.getMessageById(ctx.pathParam("message_id"));
        if(message != null){
            ctx.json(om.writeValueAsString(message));
        } else {
            ctx.json("");
        }
        ctx.status(200);
    }

    private void deleteMessageByIdHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        Message message = messageService.deleteMessageById(ctx.pathParam("message_id"));
        if(message != null){
            ctx.json(om.writeValueAsString(message));
        } else {
            ctx.json("");
        }
        ctx.status(200);
    }
}