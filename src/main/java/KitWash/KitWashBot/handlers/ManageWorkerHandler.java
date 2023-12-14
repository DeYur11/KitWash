package KitWash.KitWashBot.handlers;

import KitWash.KitWashBot.cache.Cache;
import KitWash.KitWashBot.domain.*;
import KitWash.KitWashBot.messageSender.MessageSender;
import KitWash.KitWashBot.model.Database;
import KitWash.KitWashBot.model.Worker;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Vector;

@Component
public class ManageWorkerHandler {
    private final Database database;
    private final MessageSender messageSender;
    private final EditWorkerHandler editWorkerHandler;
    private final AdminInputHandler adminInputHandler;

    private final DeleteWorkerHandler deleteWorkerHandler;
    private final Cache<BotUser> cache;

    public ManageWorkerHandler(Database database, MessageSender messageSender, EditWorkerHandler editWorkerHandler, AdminInputHandler adminInputHandler, DeleteWorkerHandler deleteWorkerHandler, Cache<BotUser> cache) {
        this.database = database;
        this.messageSender = messageSender;
        this.editWorkerHandler = editWorkerHandler;
        this.adminInputHandler = adminInputHandler;
        this.deleteWorkerHandler = deleteWorkerHandler;
        this.cache = cache;
    }

    public void generalHandler(Message message){
        BotUser botUser = cache.findBy(message.getChatId());
        switch (botUser.getManageStatus()){
            case EDITING -> editWorkerHandler.generalHandler(message);
            case DELETING -> deleteWorkerHandler.generalHandler(message);
            case ADDING -> adminInputHandler.choose(message);
            case NONE -> chooseStatus(message);
        }
    }

    private void chooseStatus(Message message){
        BotUser botUser = cache.findBy(message.getChatId());
        switch (message.getText()){
            case "Редагувати працівника" -> {
                botUser.setManageStatus(ManageStatus.EDITING);
                botUser.setWorkerEditStatus(WorkerEditStatus.NONE);

                editWorkerHandler.generalHandler(message);
            }
            case "Додати працівника" -> {
                botUser.setManageStatus(ManageStatus.ADDING);

                messageSender.sendMessage(SendMessage.builder()
                        .text("Введіть ім'я працівника")
                        .chatId(String.valueOf(botUser.getTelegramID()))
                        .build());
                botUser.setInputStatus(InputStatus.INPUT_NAME);
            }
            case "Видалити працівника" -> {
                botUser.setManageStatus(ManageStatus.DELETING);
                botUser.setDeleteWorkerStatus(DeleteWorkerStatus.NONE);
                deleteWorkerHandler.generalHandler(message);
            }
            case "Список працівників" -> {
                try {
                    outWorkers(message);
                } catch (Exception e) {
                }

                botUser.setGeneralStatus(GeneralStatus.HOME_PAGE);
                UserInputHandler.mainMenuMessage(messageSender, message);
            }
            case "На головну" -> {
                botUser.setGeneralStatus(GeneralStatus.HOME_PAGE);
                UserInputHandler.mainMenuMessage(messageSender, message);
            }
        }
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
    }
}