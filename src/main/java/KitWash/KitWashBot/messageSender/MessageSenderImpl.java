package KitWash.KitWashBot.messageSender;

import KitWash.KitWashBot.messageSender.MessageSender;
import KitWash.KitWashBot.service.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class MessageSenderImpl implements MessageSender {
    private TelegramBot helloWorldBot;

    @Override
    public void sendMessage(SendMessage sendMessage) {
        try {
            helloWorldBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    public void setHelloWorldBot(TelegramBot helloWorldBot) {
        this.helloWorldBot = helloWorldBot;
    }
}
