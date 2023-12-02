package KitWash.KitWashBot.handlers;

import KitWash.KitWashBot.cache.Cache;
import KitWash.KitWashBot.domain.BotUser;
import KitWash.KitWashBot.domain.WorkPosition;
import KitWash.KitWashBot.messageSender.MessageSender;
import KitWash.KitWashBot.model.Database;
import KitWash.KitWashBot.model.Service;
import KitWash.KitWashBot.model.ServiceType;
import org.mapdb.elsa.ElsaSerializerBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.time.LocalDateTime;
import java.util.HashMap;

@Component
public class WorkHandler {
    private final Database database;
    private final Cache<BotUser> users;
    private final MessageSender messageSender;
    private final HashMap<BotUser, Service> serviceHashMap;

    @Autowired
    public WorkHandler(Database database, Cache<BotUser> users, MessageSender messageSender) {
        this.database = database;
        this.users = users;
        this.messageSender = messageSender;
        this.serviceHashMap = new HashMap<>();
    }
    private void chooseCategory(Message message, BotUser botUser){
        switch (message.getText()){
            case "Мийка кузова" -> serviceHashMap.get(botUser).setServiceType(ServiceType.BODYWASH);
            case "Мийка кузова і салону" -> serviceHashMap.get(botUser).setServiceType(ServiceType.INTERIORBODYWASH);
            case "Хімчистка" -> serviceHashMap.get(botUser).setServiceType(ServiceType.DRYCLEANING);
        }
        serviceHashMap.get(botUser).setStartDate(LocalDateTime.now());
        try {
            database.addWorker(database.getWorkers().stream().filter(worker -> botUser.getId().equals(worker.getTelegramId())).findAny().orElse(null));
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
        messageSender.sendMessage(SendMessage.builder()
                .text("Послуга розпочата")
                .chatId(String.valueOf(botUser.getId()))
                .replyMarkup(ReplyKeyboardMarkup.builder()
                        .oneTimeKeyboard(true)
                        .resizeKeyboard(true)
                        .keyboardRow(new KeyboardRow() {{
                            add(KeyboardButton.builder()
                                    .text("Закінчити виконання")
                                    .build());
                        }}).build())
                .build());
        botUser.setWorkPosition(WorkPosition.WORKING);
    }
    private void endWork(Message message, BotUser botUser){
        messageSender.sendMessage(SendMessage.builder()
                .text("Послуга завершена")
                .chatId(String.valueOf(botUser.getId()))
                .build());
        serviceHashMap.get(botUser).setEndDate(LocalDateTime.now());
        MessageHandler.menuMessage(messageSender, message);
        database.addService(serviceHashMap.get(botUser));
        System.out.println(database.getTotalServices());
    }
    public void choose(Message message){
        BotUser botUser = users.findBy(message.getChatId());
        if(!serviceHashMap.keySet().contains(botUser)){
            serviceHashMap.put(botUser, new Service());
        }
        switch (botUser.getWorkPosition()) {
            case INPUT_CATEGORY -> chooseCategory(message, botUser);
            case WORKING ->endWork(message, botUser);
        }

    }
}
