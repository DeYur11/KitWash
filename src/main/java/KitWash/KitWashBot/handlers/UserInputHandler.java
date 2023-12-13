package KitWash.KitWashBot.handlers;

import KitWash.KitWashBot.cache.Cache;
import KitWash.KitWashBot.domain.InputStatus;
import KitWash.KitWashBot.domain.BotUser;
import KitWash.KitWashBot.domain.GeneralStatus;
import KitWash.KitWashBot.domain.WorkStatus;
import KitWash.KitWashBot.messageSender.MessageSender;
import KitWash.KitWashBot.model.Database;
import KitWash.KitWashBot.model.Worker;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Vector;

@Component
public class UserInputHandler {
    private final MessageSender messageSender;
    private final Cache<BotUser> cache;
    private final AdminInputHandler adminInputHandler;
    private final ServiceInputHandler serviceInputHandler;
    private final Database database;

    //конструктор класу UserInputHandler
    public UserInputHandler(MessageSender messageSender, Database database, AdminInputHandler adminInputHandler, Cache<BotUser> cache, ServiceInputHandler serviceInputHandler){
        this.messageSender = messageSender;
        this.database = database;
        this.cache = cache;
        this.adminInputHandler = adminInputHandler;
        this.serviceInputHandler = serviceInputHandler;

        BotUser Yura = new BotUser(708874243L);
        Yura.setGeneralStatus(GeneralStatus.HOME_PAGE);
        cache.add(Yura);

        BotUser Ivan = new BotUser(343523935L);
        Ivan.setGeneralStatus(GeneralStatus.HOME_PAGE);
        cache.add(Ivan);
    }

    //функція обробки вводу користувача при роботі з ботом
    public void choose(Message message){
        BotUser botUser = cache.findBy(message.getChatId());
        if(botUser !=null){
            switch (botUser.getGeneralStatus()){
                case HOME_PAGE:
                    String text = message.getText();
                    UserInputHandler.menuMessage(messageSender, message);
                    botUser.setGeneralStatus(GeneralStatus.NONE);
                    break;
                case NONE:
                    switch (message.getText()){
                        case "Додати працівника":
                            botUser.setGeneralStatus(GeneralStatus.ADDING);
                            botUser.setInputStatus(InputStatus.INPUT_NAME);
                            messageSender.sendMessage(SendMessage.builder()
                                    .text("Введіть ім'я працівника")
                                    .chatId(String.valueOf(botUser.getTelegramID()))
                                    .build());
                            break;
                        case "Розпочати послугу":
                            botUser.setGeneralStatus(GeneralStatus.WORKING);
                            botUser.setWorkStatus(WorkStatus.INPUT_CATEGORY);
                            messageSender.sendMessage(SendMessage.builder()
                                    .text("Виберіть послугу, яку надаєте:")
                                    .chatId(String.valueOf(botUser.getTelegramID()))
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
                        case "Список працівників":

                            Vector<Worker> workers = database.getWorkers();
                            String MessageBody= "";
                            if (workers.size() != 0) {

                                MessageBody = "Список працівників:\n";
                                for (int i = 0; i < workers.size(); i++) {
                                    MessageBody  = MessageBody.concat((i + 1) + ". " + workers.get(i).outString());
                                }
                            }
                            else{
                                MessageBody = "Список працівників пустий\n";
                            }
                            messageSender.sendMessage(SendMessage.builder()
                                    .text(MessageBody)
                                    .chatId(String.valueOf(botUser.getTelegramID()))
                                    .build());
                            UserInputHandler.menuMessage(messageSender, message);
                            break;
                    }
                    break;
                case ADDING:
                    adminInputHandler.choose(message);
                    break;
                case WORKING:
                    serviceInputHandler.choose(message);
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

    //функція побудови інтерактивного меню користувача
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
                                    add(KeyboardButton.builder().
                                            text("Список працівників")
                                            .build());
                                }}).build())
                        .build()
        );
    }

}
