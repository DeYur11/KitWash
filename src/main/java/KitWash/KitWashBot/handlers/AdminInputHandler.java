package KitWash.KitWashBot.handlers;

import KitWash.KitWashBot.cache.Cache;
import KitWash.KitWashBot.domain.BotUser;
import KitWash.KitWashBot.domain.GeneralStatus;
import KitWash.KitWashBot.messageSender.MessageSender;
import KitWash.KitWashBot.model.Database;
import KitWash.KitWashBot.model.Worker;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.HashMap;

import static KitWash.KitWashBot.domain.InputStatus.*;

@Component
public class AdminInputHandler {
    private final Database database;
    private final MessageSender messageSender;
    private final Cache<BotUser> cache;
    private final HashMap<BotUser, AddForm> addingUsers;

    //конструктор класу AdminInputHandler
    public AdminInputHandler(MessageSender messageSender, Cache<BotUser> cache, Database database){
        this.messageSender = messageSender;
        this.database = database;
        this.cache = cache;
        addingUsers = new HashMap<>();
    }

    //функція обробки вводу Адміністратора при додаванні Користувача
    public void choose(Message message){

        BotUser botUser = cache.findBy(message.getChatId());

        if(!addingUsers.keySet().contains(botUser)){
            addingUsers.put(botUser, new AddForm());
        }

        switch (botUser.getInputStatus()){
            case INPUT_NAME:

                botUser.setInputStatus(INPUT_SURNAME);
                addingUsers.get(botUser).setName(message.getText());
                messageSender.sendMessage(SendMessage.builder()
                        .text("Введіть прізвище працівника")
                        .chatId(String.valueOf(botUser.getTelegramID()))
                        .build());
                break;
            case INPUT_SURNAME:
                botUser.setInputStatus(INPUT_ID);
                addingUsers.get(botUser).setSurname(message.getText());
                messageSender.sendMessage(SendMessage.builder()
                        .text("Введіть ID користувача")
                        .chatId(String.valueOf(botUser.getTelegramID()))
                        .build());
                break;
            case INPUT_ID:
                botUser.setGeneralStatus(GeneralStatus.START_PAGE);
                addingUsers.get(botUser).setId(Long.parseLong(message.getText()));
                messageSender.sendMessage(SendMessage.builder()
                        .text("Працівник успішно доданий")
                        .chatId(String.valueOf(botUser.getTelegramID()))
                        .build());

                botUser.setWorker(new Worker(
                        addingUsers.get(botUser).getName(),
                        addingUsers.get(botUser).getSurname(),
                        addingUsers.get(botUser).getId()
                ));
                try {
                    database.addWorker(botUser.getWorker());
                }catch (Exception e){
                    e.printStackTrace();
                }
                System.out.println(database.getWorkers());
                cache.add(new BotUser(addingUsers.get(botUser).getId(), GeneralStatus.START_PAGE));
                messageSender.sendMessage(SendMessage.builder()
                        .text("Ви додані\n")
                        .chatId(addingUsers.get(botUser).getId())
                        .replyMarkup(ReplyKeyboardMarkup.builder()
                                .oneTimeKeyboard(true)
                                .resizeKeyboard(true)
                                .keyboardRow(new KeyboardRow() {{
                                    add(KeyboardButton.builder()
                                            .text("Підтвердити")
                                            .build());
                                }}).build())
                        .build());
                break;
        }

    }

}
