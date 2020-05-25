package finder.bot;

import finder.model.User;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import finder.service.UserService;

import java.io.InputStream;

@Component
@PropertySource("classpath:telegram.properties")
public class ChatBot extends TelegramLongPollingBot {

    private static final Logger LOGGER = LogManager.getLogger(ChatBot.class);

    private static final String BROADCAST = "broadcast ";
    private static final String LIST_USERS = "users";
    private static final String HELP = "/help";
    private static final String START = "/start";


    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String botToken;

    private final UserService userService;

    public ChatBot(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText())
            return;

        final String text = update.getMessage().getText();
        final long chatId = update.getMessage().getChatId();

        User user = userService.findByChatId(chatId);

        if (checkIfAdminCommand(user, text))
            return;

        BotContext context;
        BotState state;

        if (user == null) {
            state = BotState.getInitialState();

            user = new User(chatId, state.ordinal());
            userService.addUser(user);

            context = BotContext.of(this, user, text);
            state.enter(context,userService);

            LOGGER.info("New user registered: " + chatId);
        } else {
            if (text.equals("/start")){
                context = BotContext.of(this, user, text);
                state = BotState.byId(0);
            }else {
                context = BotContext.of(this, user, text);
                state = BotState.byId(user.getStateId());
            }
            LOGGER.info("Update received for user in state: " + state);
        }

        state.handleInput(context,userService);

        do {
            state = state.nextState(context);
           /* userService.updateUser(user);*/
         try {
             state.enter(context,userService);
         }catch (NullPointerException n) {
             System.out.println("Its just finish!");
         }
        } while (!state.isInputNeeded());

        user.setStateId(state.ordinal());
        userService.updateUser(user);
    }

    private boolean checkIfAdminCommand(User user, String text) {
        if (user == null)
            return false;

        if (user.getAdmin()) {
            if (text.startsWith(BROADCAST)) {
                LOGGER.info("Admin command received: " + BROADCAST);

                text = text.substring(BROADCAST.length());
               /* broadcast(text);*/

                return true;
            } else if (text.equals(LIST_USERS)) {
                LOGGER.info("Admin command received: " + LIST_USERS);

                /*  listUsers(user);*/
                return true;
            } else {
                if (text.startsWith("/start")) {
                    sendMessage(user.getChatId(), " Я допоможу тобі, а може і ні!");

                    return true;
                }
            }
        }
        return false;
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage()
                .setChatId(chatId)
                .setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendPhoto(long chatId) {
        InputStream is = getClass().getClassLoader()
                .getResourceAsStream("test.png");

        SendPhoto message = new SendPhoto()
                .setChatId(chatId)
                .setPhoto("test", is);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

   /* private void listUsers(User admin) {
        StringBuilder sb = new StringBuilder("All users list:\r\n");
        List<User> users = userService.findAllUsers();

        users.forEach(user ->
            sb
                    .append(user.getId())
                    .append(' ')
                    .append(user.getName())
                    .append(' ')
                    .append(user.getPhone())
                    .append(' ')
                    .append(user.getEmail())
                    .append("\r\n")
        );

        sendPhoto(admin.getChatId());
        sendMessage(admin.getChatId(), sb.toString());
    }*/

   /* private void broadcast(String text) {
        List<User> users = userService.findAllUsers();
        users.forEach(user -> sendMessage(user.getChatId(), text));
    }*/
}
