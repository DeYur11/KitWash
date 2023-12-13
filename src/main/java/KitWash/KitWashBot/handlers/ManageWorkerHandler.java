package KitWash.KitWashBot.handlers;

import KitWash.KitWashBot.cache.Cache;
import KitWash.KitWashBot.domain.*;
import KitWash.KitWashBot.messageSender.MessageSender;
import KitWash.KitWashBot.model.Database;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

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
        }
    }
}