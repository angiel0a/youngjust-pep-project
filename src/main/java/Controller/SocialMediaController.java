package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Service.AccountService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    AccountService accountService; 

    public SocialMediaController(){
        this.accountService = new AccountService();
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
}