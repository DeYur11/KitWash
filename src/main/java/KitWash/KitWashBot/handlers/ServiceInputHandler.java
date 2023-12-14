package KitWash.KitWashBot.handlers;

import KitWash.KitWashBot.cache.Cache;
import KitWash.KitWashBot.domain.BotUser;
import KitWash.KitWashBot.domain.GeneralStatus;
import KitWash.KitWashBot.domain.WorkStatus;
import KitWash.KitWashBot.messageSender.MessageSender;
import KitWash.KitWashBot.model.Database;
import KitWash.KitWashBot.model.Service;
import KitWash.KitWashBot.model.ServiceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Vector;

@Component
public class ServiceInputHandler {
    private final Database database;
    private final Cache<BotUser> users;
    private final MessageSender messageSender;
    private final HashMap<BotUser, Service> serviceHashMap;

    //конструктор класу ServiceInputHandler
    @Autowired
    public ServiceInputHandler(Database database, Cache<BotUser> users, MessageSender messageSender) {
        this.database = database;
        this.users = users;
        this.messageSender = messageSender;
        this.serviceHashMap = new HashMap<>();
    }

    //функція обробки вводу користувача при роботі з послугами
    public void choose(Message message){
        BotUser botUser = users.findBy(message.getChatId());
        if(!serviceHashMap.keySet().contains(botUser)){
            serviceHashMap.put(botUser, new Service());
        }
        switch (botUser.getWorkStatus()) {
            case INPUT_CATEGORY -> chooseCategory(message, botUser);
            case WORKING ->endWork(message, botUser);

        }

    }

    //функція початку послуги з попереднім вибором категорії та робітника
    private void chooseCategory(Message message, BotUser botUser){
        serviceHashMap.replace(botUser, new Service());
        switch (message.getText()){
            case "Мийка кузова" -> serviceHashMap.get(botUser).setServiceType(ServiceType.BODYWASH);
            case "Мийка кузова і салону" -> serviceHashMap.get(botUser).setServiceType(ServiceType.INTERIORBODYWASH);
            case "Хімчистка" -> serviceHashMap.get(botUser).setServiceType(ServiceType.DRYCLEANING);
        }
        serviceHashMap.get(botUser).setStartDate(LocalTime.now());
        try {
            serviceHashMap.get(botUser).addWorker(database.getWorkers().stream().filter(worker -> botUser.getTelegramID().equals(worker.getTelegramId())).findAny().orElse(null));
            }catch (Exception e){
            e.printStackTrace();
            return;
        }
        messageSender.sendMessage(SendMessage.builder()
                .text("Послуга розпочата")
                .chatId(String.valueOf(botUser.getTelegramID()))
                .replyMarkup(ReplyKeyboardMarkup.builder()
                        .oneTimeKeyboard(true)
                        .resizeKeyboard(true)
                        .keyboardRow(new KeyboardRow() {{
                            add(KeyboardButton.builder()
                                    .text("Закінчити виконання")
                                    .build());
                        }}).build())
                .build());
        botUser.setWorkStatus(WorkStatus.WORKING);
    }

    //функція завершення розпочатої послуги
    private void endWork(Message message, BotUser botUser){
        messageSender.sendMessage(SendMessage.builder()
                .text("Послуга завершена")
                .chatId(String.valueOf(botUser.getTelegramID()))
                .build());
        serviceHashMap.get(botUser).setEndDate(LocalTime.now());
        botUser.setGeneralStatus(GeneralStatus.HOME_PAGE);
        UserInputHandler.mainMenuMessage(messageSender, message);
        database.addService(serviceHashMap.get(botUser));
        System.out.println(database.getTotalServices());
    }
}
