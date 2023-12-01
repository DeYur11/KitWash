package KitWash.KitWashBot.handlers;

import KitWash.KitWashBot.cache.Cache;
import KitWash.KitWashBot.domain.AddPosition;
import KitWash.KitWashBot.domain.Position;
import KitWash.KitWashBot.domain.User;
import KitWash.KitWashBot.messageSender.MessageSender;
import KitWash.KitWashBot.model.Database;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

@Component
public class MessageHandler {
    private final MessageSender messageSender;
    private final Cache<User> cache;
    private final AddUserHandler addUserHandler;
    private final Database database;

    public MessageHandler(MessageSender messageSender, Database database, AddUserHandler addUserHandler, Cache<User> cache){
        this.messageSender = messageSender;
        this.database = database;
        this.cache = cache;
        this.addUserHandler = addUserHandler;
        User admin = new User(708874243L);
        admin.setPosition(Position.HOME_PAGE);
        cache.add(admin);
    }

    public void choose(Message message){
        User user = cache.findBy(message.getChatId());
        if(user!=null){
            switch (user.getPosition()){
                case HOME_PAGE:
                    String text = message.getText();
                    messageSender.sendMessage(
                            SendMessage.builder()
                                    .text("Головна сторінка")
                                    .chatId(String.valueOf(message.getChatId()))
                                    .replyMarkup(ReplyKeyboardMarkup.builder()
                                            .oneTimeKeyboard(true)
                                            .resizeKeyboard(true)
                                            .keyboardRow(new KeyboardRow() {{
                                                add(KeyboardButton.builder()
                                                        .text("Додати працівника")
                                                        .build());
                                            }}).build())
                                    .build()
                    );
                    user.setPosition(Position.NONE);
                    break;
                case NONE:
                    switch (message.getText()){
                        case "Додати працівника":
                            user.setPosition(Position.ADDING);
                            user.setAddPosition(AddPosition.INPUT_NAME);
                            messageSender.sendMessage(SendMessage.builder()
                                    .text("Введіть ім'я працівника")
                                    .chatId(String.valueOf(user.getId()))
                                    .build());
                            break;
                    }
                    break;
                case ADDING:
                    addUserHandler.choose(message);
                    break;
            }

        }else if (message.hasText()){
            var sendMessage = SendMessage.builder()
                    .text("Please wait for administrator to add you.\n" +
                            "Your telegram ID: " + message.getChatId())

                    .chatId(String.valueOf(message.getChatId()))
                    .build();
            messageSender.sendMessage(sendMessage);
        }
    }

}
