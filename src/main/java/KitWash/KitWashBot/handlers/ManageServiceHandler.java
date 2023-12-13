package KitWash.KitWashBot.handlers;

import KitWash.KitWashBot.cache.Cache;
import KitWash.KitWashBot.domain.*;
import KitWash.KitWashBot.messageSender.MessageSender;
import KitWash.KitWashBot.model.Database;
import KitWash.KitWashBot.model.Service;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Vector;

@Component
public class ManageServiceHandler {
    private final Database database;
    private final MessageSender messageSender;
    private final EditServiceHandler editServiceHandler;
    private final AdminInputHandler adminInputHandler;
    private final Cache<BotUser> cache;

    public ManageServiceHandler(Database database, MessageSender messageSender, EditServiceHandler editServiceHandler, AdminInputHandler adminInputHandler, Cache<BotUser> cache) {
        this.database = database;
        this.messageSender = messageSender;
        this.editServiceHandler = editServiceHandler;
        this.adminInputHandler = adminInputHandler;
        this.cache = cache;
    }

    public void generalHandler(Message message){
        BotUser botUser = cache.findBy(message.getChatId());
        switch (botUser.getManageStatus()){
            case EDITING -> editServiceHandler.generalHandler(message);
            case DELETING -> {
                return;
            }
            case ADDING -> adminInputHandler.choose(message);
            case NONE -> chooseStatus(message);
        }
    }

    private void chooseStatus(Message message){
        BotUser botUser = cache.findBy(message.getChatId());
        switch (message.getText()){
            case "Редагувати послугу" -> {
                botUser.setManageStatus(ManageStatus.EDITING);
                botUser.setServiceEditStatus(ServiceEditStatus.NONE);

                editServiceHandler.generalHandler(message);
            }
/*            case "Список послуг" ->{
                botUser.setManageStatus(ManageStatus.REVIEW);

                try {
                    outServicesList(message);
                }
                catch (Exception exc){
                    UserInputHandler.mainMenuMessage(messageSender, message);
                    botUser.setGeneralStatus(GeneralStatus.HOME_PAGE);
                    return;
                }
            }*/
        }
    }
/*    private void outServicesList(Message message) throws Exception {
        BotUser botUser = cache.findBy(message.getChatId());
        Vector<Service> servicesList = database.getTotalServices();
        String MessageBody= "";
        if (servicesList.size() != 0) {

            MessageBody = "Список наданих послуг:\n";
            for (int i = 0; i < servicesList.size(); i++) {
                MessageBody  = MessageBody.concat((i + 1) + ". " + servicesList.get(i).outString());
            }
*//*            System.out.println(MessageBody);
            System.out.println(servicesList.size());*//*
        }
        else{
            messageSender.sendMessage(SendMessage.builder()
                    .text("Cписок пустий")
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
    }*/
}
