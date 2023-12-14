package KitWash.KitWashBot.handlers;


import KitWash.KitWashBot.cache.Cache;
import KitWash.KitWashBot.domain.BotUser;
import KitWash.KitWashBot.domain.DeleteServiceStatus;
import KitWash.KitWashBot.domain.GeneralStatus;
import KitWash.KitWashBot.domain.ServiceEditStatus;
import KitWash.KitWashBot.messageSender.MessageSender;
import KitWash.KitWashBot.model.Database;
import KitWash.KitWashBot.model.Service;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Vector;

import static KitWash.KitWashBot.domain.DeleteServiceStatus.DELETE_SERVICE;

@Component
public class DeleteServiceHandler {
    private final Database database;
    private final MessageSender messageSender;
    private final Cache<BotUser> cache;

    public DeleteServiceHandler(Database database, MessageSender messageSender, Cache<BotUser> cache) {
        this.database = database;
        this.messageSender = messageSender;
        this.cache = cache;
    }
    private void outServicesList(Message message) throws Exception {

        BotUser botUser = cache.findBy(message.getChatId());
        Vector<Service> servicesList = database.getTotalServices();
        String MessageBody= "";
        if (servicesList.size() != 0) {

            MessageBody = "Список наданих послуг:\n";
            for (int i = 0; i < servicesList.size(); i++) {
                MessageBody  = MessageBody.concat((i + 1) + ". " + servicesList.get(i).outString());
            }
        }
        else{
            messageSender.sendMessage(SendMessage.builder()
                    .text("Cписок пустий, редагування неможливе")
                    .chatId(String.valueOf(botUser.getTelegramID()))
                    .build());
            botUser.setGeneralStatus(GeneralStatus.HOME_PAGE);
            UserInputHandler.mainMenuMessage(messageSender, message);
            throw new Exception();
        }
        messageSender.sendMessage(SendMessage.builder()
                .text(MessageBody)
                .chatId(String.valueOf(botUser.getTelegramID()))
                .build());
    }


    public void chooseService(Message message){
        BotUser botUser = cache.findBy(message.getChatId());
        try {
            outServicesList(message); // Виводимо працівників
        } catch (Exception e) {
            UserInputHandler.mainMenuMessage(messageSender, message);
            botUser.setGeneralStatus(GeneralStatus.HOME_PAGE);
            return;
        }
        messageSender.sendMessage(SendMessage.builder()
                .text("Виберіть послугу зі списку, вписавши її номер: ")
                .chatId(String.valueOf(botUser.getTelegramID()))
                .build());

        botUser.setDeleteServiceStatus(DeleteServiceStatus.DELETE_SERVICE);
    }
    private void deleteService(Message message){
        BotUser botUser = cache.findBy(message.getChatId());
        try {
            int index = Integer.parseInt(message.getText());
            database.getTotalServices().remove(index-1);
        }catch (Exception e){
            System.out.println("Error");
        }

        botUser.setGeneralStatus(GeneralStatus.HOME_PAGE);
        UserInputHandler.mainMenuMessage(messageSender, message);
    }

    public void generalHandler(Message message){
        BotUser botUser = cache.findBy(message.getChatId());

        switch (botUser.getDeleteServiceStatus()){
            case NONE -> chooseService(message);
            case DELETE_SERVICE -> deleteService(message);
        }
    }
}
