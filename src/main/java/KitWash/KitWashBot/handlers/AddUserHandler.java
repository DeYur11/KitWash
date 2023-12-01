package KitWash.KitWashBot.handlers;

import KitWash.KitWashBot.cache.Cache;
import KitWash.KitWashBot.domain.AddPosition;
import KitWash.KitWashBot.domain.Position;
import KitWash.KitWashBot.domain.User;
import KitWash.KitWashBot.messageSender.MessageSender;
import KitWash.KitWashBot.model.Database;
import KitWash.KitWashBot.model.Worker;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import javax.xml.crypto.Data;
import java.util.HashMap;

import static KitWash.KitWashBot.domain.AddPosition.*;

@Component
public class AddUserHandler {
    private final Database database;
    private final MessageSender messageSender;
    private final Cache<User> cache;
    private final HashMap<User, AddForm> addingUsers;
    public AddUserHandler(MessageSender messageSender, Cache<User> cache, Database database){
        this.messageSender = messageSender;
        this.database = database;
        this.cache = cache;
        addingUsers = new HashMap<>();
    }

    public void choose(Message message){

        User user = cache.findBy(message.getChatId());

        if(!addingUsers.keySet().contains(user)){
            addingUsers.put(user, new AddForm());
        }

        switch (user.getAddPosition()){
            case INPUT_NAME:

                user.setAddPosition(INPUT_SURNAME);
                addingUsers.get(user).setName(message.getText());
                messageSender.sendMessage(SendMessage.builder()
                        .text("Введіть прізвище працівника")
                        .chatId(String.valueOf(user.getId()))
                        .build());
                break;
            case INPUT_SURNAME:
                user.setAddPosition(INPUT_ID);
                addingUsers.get(user).setSurname(message.getText());
                messageSender.sendMessage(SendMessage.builder()
                        .text("Введіть ID користувача")
                        .chatId(String.valueOf(user.getId()))
                        .build());
                break;
            case INPUT_ID:
                user.setPosition(Position.HOME_PAGE);
                addingUsers.get(user).setId(Long.parseLong(message.getText()));
                messageSender.sendMessage(SendMessage.builder()
                        .text("Працівник успішно доданий")
                        .chatId(String.valueOf(user.getId()))
                        .build());

                user.setWorker(new Worker(
                        addingUsers.get(user).getName(),
                        addingUsers.get(user).getSurname(),
                        addingUsers.get(user).getId()
                ));
                try {
                    database.addWorker(user.getWorker());
                }catch (Exception e){
                    e.printStackTrace();
                }
                System.out.println(database.getWorkers());
                cache.add(new User(addingUsers.get(user).getId(), Position.HOME_PAGE));
                messageSender.sendMessage(SendMessage.builder()
                        .text("You are added\n")
                        .chatId(addingUsers.get(user).getId())
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
