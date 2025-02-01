package Controller;

import org.h2.util.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.JsonEOFException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
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
    public SocialMediaController(){
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
        app.post("/register", this::postRegisterHandler);
        app.post("/login", this::postLoginHandler);
        app.post("/messages", this::postCreateMessageHandler);
        app.get("/messages", this::getMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageIDHandler);
        app.delete("/messages/{message_id}", this::deleteMessageIDHandler);
        app.patch("/messages/{message_id}", this::patchMessageByIDHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesByUserHandler);
        return app;
    }

    private void postRegisterHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        if(addedAccount == null) {
            ctx.status(400);
        }
        else {
            ctx.json(mapper.writeValueAsString(addedAccount));
        }
    }

    private void postLoginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account loggedInAccount = accountService.logInAccount(account);
        if(loggedInAccount == null){
            ctx.status(401);
        }
        else
        {
            ctx.json(mapper.writeValueAsString(loggedInAccount));
        }
    }

    private void postCreateMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message createdMessage = messageService.createMessage(message);
        if(createdMessage == null){
            ctx.status(400);
        }
        else
        {
            ctx.json(mapper.writeValueAsString(createdMessage));
        }
    }

    private void getMessagesHandler(Context ctx) {
        ctx.json(messageService.getAllMessages());
    }

    private void getMessageIDHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = messageService.getMessageById(ctx.pathParam("message_id"));
        if(message != null){
            ctx.json(mapper.writeValueAsString(message));
        }
        else{
            ctx.result("");
        }
    }

    private void deleteMessageIDHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = messageService.deleteMessageByID(ctx.pathParam("message_id"));
        if(message != null){
            ctx.json(mapper.writeValueAsString(message));
        }
        else
        {
            ctx.status(200);
            ctx.result("");
        }
    }

    private void patchMessageByIDHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        String body = ctx.body();
        body = body.substring(body.indexOf("\"", 17)+1, body.indexOf("\"", 18));
        Message message = messageService.patchMessageByID(ctx.pathParam("message_id"), body);
        if(message == null){
            ctx.status(400);
        }
        else
        {
            ctx.json(mapper.writeValueAsString(message));
        }
    }

    private void getMessagesByUserHandler(Context ctx) throws JsonProcessingException{
        ctx.json(messageService.getMessagesByUser(Integer.parseInt(ctx.pathParam("account_id"))));
    }

}