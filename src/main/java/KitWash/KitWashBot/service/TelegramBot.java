package KitWash.KitWashBot.service;


import KitWash.KitWashBot.config.BotConfig;
import KitWash.KitWashBot.handlers.UserInputHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    final BotConfig config;
    private SendMessageService sendMessageService;
    private UserInputHandler userInputHandler;

    //конструктор класу TelegramBot
    public TelegramBot(BotConfig config) {
        this.config = config;
    }

    //геттери класу TelegramBot
    @Override
    public String getBotUsername() {
        return config.getBotName();
    }
    @Override
    public String getBotToken() {
        return config.getToken();
    }

    //сеттери класу TelegramBot
    @Autowired
    public void setSendMessageService(SendMessageService sendMessageService) {
        this.sendMessageService = sendMessageService;
    }
    @Autowired
    public void setMessageHandler(UserInputHandler userInputHandler) {
        this.userInputHandler = userInputHandler;
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            userInputHandler.choose(update.getMessage());
//                switch (messageText) {
//                    case "/start":
//                        startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
//                        break;
//
//                    default:
//                        prepareAndSendMessage(chatId, "Sorry, command was not recognized");
//                }
        }
    }

    private void startCommandReceived(long chatId, String name) {


        String answer ="Hi, " + name + ", nice to meet you!";
        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        executeMessage(message);
    }

    private void executeMessage(SendMessage message){
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    private void prepareAndSendMessage(long chatId, String textToSend){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        executeMessage(message);
    }

}