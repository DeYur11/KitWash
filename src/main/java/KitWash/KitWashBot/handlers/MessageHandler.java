package KitWash.KitWashBot.handlers;

import KitWash.KitWashBot.cache.Cache;
import KitWash.KitWashBot.domain.AddPosition;
import KitWash.KitWashBot.domain.BotUser;
import KitWash.KitWashBot.domain.Position;
import KitWash.KitWashBot.domain.WorkPosition;
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
    private final Cache<BotUser> cache;
    private final AddUserHandler addUserHandler;
    private final WorkHandler workHandler;
    private final Database database;

    public MessageHandler(MessageSender messageSender, Database database, AddUserHandler addUserHandler, Cache<BotUser> cache, WorkHandler workHandler){
        this.messageSender = messageSender;
        this.database = database;
        this.cache = cache;
        this.addUserHandler = addUserHandler;
        this.workHandler = workHandler;
        BotUser admin = new BotUser(708874243L);
        admin.setPosition(Position.HOME_PAGE);
        cache.add(admin);
    }

    public void choose(Message message){
        BotUser botUser = cache.findBy(message.getChatId());
        if(botUser !=null){
            switch (botUser.getPosition()){
                case HOME_PAGE:
                    String text = message.getText();
                    MessageHandler.menuMessage(messageSender, message);
                    botUser.setPosition(Position.NONE);
                    break;
                case NONE:
                    switch (message.getText()){
                        case "Додати працівника":
                            botUser.setPosition(Position.ADDING);
                            botUser.setAddPosition(AddPosition.INPUT_NAME);
                            messageSender.sendMessage(SendMessage.builder()
                                    .text("Введіть ім'я працівника")
                                    .chatId(String.valueOf(botUser.getId()))
                                    .build());
                            break;
                        case "Розпочати послугу":
                            botUser.setPosition(Position.WORKING);
                            botUser.setWorkPosition(WorkPosition.INPUT_CATEGORY);
                            messageSender.sendMessage(SendMessage.builder()
                                    .text("Виберіть послугу, яку надаєте:")
                                    .chatId(String.valueOf(botUser.getId()))
                                    .replyMarkup(ReplyKeyboardMarkup.builder()
                                            .oneTimeKeyboard(true)
                                            .resizeKeyboard(true)
                                            .keyboardRow(new KeyboardRow() {{
                                                add(KeyboardButton.builder()
                                                        .text("Мийка кузова")
                                                        .build());
                                                add(KeyboardButton.builder()
                                                        .text("Мийка кузова і салону")
                                                        .build());
                                                add(KeyboardButton.builder()
                                                        .text("Хімчистка")
                                                        .build());
                                            }}).build())
                                    .build());
                            break;
                    }
                    break;
                case ADDING:
                    addUserHandler.choose(message);
                    break;
                case WORKING:
                    workHandler.choose(message);
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
    public static void menuMessage(MessageSender messageSender, Message message){
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
                                    add(KeyboardButton.builder().
                                            text("Розпочати послугу")
                                            .build());
                                }}).build())
                        .build()
        );
    }

}
