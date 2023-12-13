package KitWash.KitWashBot.handlers;

import KitWash.KitWashBot.cache.Cache;
import KitWash.KitWashBot.domain.BotUser;
import KitWash.KitWashBot.domain.DeleteWorkerStatus;
import KitWash.KitWashBot.domain.GeneralStatus;
import KitWash.KitWashBot.domain.WorkerEditStatus;
import KitWash.KitWashBot.messageSender.MessageSender;
import KitWash.KitWashBot.model.Database;
import KitWash.KitWashBot.model.Worker;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.HashMap;
import java.util.Vector;

@Component
public class DeleteWorkerHandler {
    private final Database database;
    private final MessageSender messageSender;
    private final Cache<BotUser> cache;
    private final HashMap<BotUser, AddForm> addingUsers;

    public DeleteWorkerHandler(Database database, MessageSender messageSender, Cache<BotUser> cache, HashMap<BotUser, AddForm> addingUsers) {
        this.database = database;
        this.messageSender = messageSender;
        this.cache = cache;
        this.addingUsers = addingUsers;
    }
    private void outWorkers(Message message) throws Exception {
        BotUser botUser = cache.findBy(message.getChatId());
        Vector<Worker> workers = database.getWorkers();
        String MessageBody= "";
        if (workers.size() != 0) {

            MessageBody = "Список працівників:\n";
            for (int i = 0; i < workers.size(); i++) {
                MessageBody  = MessageBody.concat((i + 1) + ". " + workers.get(i).outString());
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


    public void chooseWorker(Message message){
        BotUser botUser = cache.findBy(message.getChatId());
        Vector<Worker> workers = database.getWorkers();

        try {
            outWorkers(message); // Виводимо працівників
        } catch (Exception e) {
            UserInputHandler.mainMenuMessage(messageSender, message);
            botUser.setGeneralStatus(GeneralStatus.HOME_PAGE);
            return;
        }

        messageSender.sendMessage(SendMessage.builder()
                .text("Виберіть працівника зі списку, вписавши його номер: ")
                .chatId(String.valueOf(botUser.getTelegramID()))
                .build());

        botUser.setDeleteWorkerStatus(DeleteWorkerStatus.DELETE_WORKER);
    }
    private void deleteWorker(Message message){
        BotUser botUser = cache.findBy(message.getChatId());
        try {
            for(var i: cache.getAll()){
                if(i.getTelegramID().equals(botUser.getWorker().getTelegramId())){
                    cache.remove(i);
                    database.getWorkers().remove(botUser.getWorker());
                }
            }
        }catch (Exception e){
            System.out.println("Error");
        }

        try {
            UserInputHandler.mainMenuMessage(messageSender, message);
            botUser.setGeneralStatus(GeneralStatus.HOME_PAGE);
        }catch (Exception e){

        }

    }

    public void generalHandler(Message message){
        BotUser botUser = cache.findBy(message.getChatId());

        switch (botUser.getDeleteWorkerStatus()){
            case NONE -> chooseWorker(message);
            case DELETE_WORKER -> deleteWorker(message);
        }
    }

}
