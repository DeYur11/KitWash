package KitWash.KitWashBot.service;

import KitWash.KitWashBot.messageSender.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
public class SendMessageService {
    private MessageSender messageSender;

    @Autowired
    public void setMessageSender(@Lazy MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    public void test1(Message message){
        var ms1 = SendMessage.builder()
                .text("Hello world")
                .chatId(String.valueOf(message.getChatId()))
                .build();
        messageSender.sendMessage(ms1);
    }
}
