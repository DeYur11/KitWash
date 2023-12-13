package KitWash.KitWashBot.handlers;

import KitWash.KitWashBot.cache.Cache;
import KitWash.KitWashBot.domain.InputStatus;
import KitWash.KitWashBot.domain.BotUser;
import KitWash.KitWashBot.domain.GeneralStatus;
import KitWash.KitWashBot.domain.WorkStatus;
import KitWash.KitWashBot.messageSender.MessageSender;
import KitWash.KitWashBot.model.Database;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

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
                    mainMenuMessage(messageSender, message);
                    botUser.setGeneralStatus(GeneralStatus.NONE);
                    break;
                case NONE:
                    switch (message.getText()){
                        case "Розпочати послугу":
                            botUser.setGeneralStatus(GeneralStatus.WORKING);
                            botUser.setWorkStatus(WorkStatus.INPUT_CATEGORY);
                            startServiceMenuMessage(messageSender, message);
                            break;
                        case "Управління працівниками":
                            botUser.setGeneralStatus(GeneralStatus.ADDING);
                            botUser.setInputStatus(InputStatus.INPUT_NAME);
                            workerMenuMessage(messageSender, message);
                            break;
                        case "Управління послугами":
                            serviceMenuMessage(messageSender, message);
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
    public static void mainMenuMessage(MessageSender messageSender, Message message){
        messageSender.sendMessage(
                SendMessage.builder()
                        .text("Головна сторінка")
                        .chatId(String.valueOf(message.getChatId()))
                        .replyMarkup(ReplyKeyboardMarkup.builder()
                                .oneTimeKeyboard(true)
                                .resizeKeyboard(true)
                                .keyboardRow(new KeyboardRow() {{
                                    add(KeyboardButton.builder()
                                            .text("Розпочати послугу")
                                            .build());
                                }})
                                .keyboardRow(new KeyboardRow(){{
                                    add(KeyboardButton.builder()
                                            .text("Управління послугами")
                                            .build());
                                    add(KeyboardButton.builder()
                                            .text("Управління працівниками")
                                            .build());
                                }})
                                .build())
                        .build()
        );
    }

    public static void workerMenuMessage(MessageSender messageSender, Message message){
        messageSender.sendMessage(
                SendMessage.builder()
                        .text("Редагування працівників")
                        .chatId(String.valueOf(message.getChatId()))
                        .replyMarkup(ReplyKeyboardMarkup.builder()
                                .oneTimeKeyboard(true)
                                .resizeKeyboard(true)
                                .keyboardRow(new KeyboardRow(){{
                                    add(KeyboardButton.builder()
                                            .text("Список працівників")
                                            .build());
                                }})
                                .keyboardRow(new KeyboardRow() {{
                                    add(KeyboardButton.builder()
                                            .text("Додати працівника")
                                            .build());
                                    add(KeyboardButton.builder()
                                            .text("Редагувати працівника")
                                            .build());
                                    add(KeyboardButton.builder()
                                            .text("Видалити працівника")
                                            .build());
                                }})
                                .keyboardRow(new KeyboardRow(){{
                                    add(KeyboardButton.builder()
                                            .text("На головну")
                                            .build());

                                }})

                                .build())
                        .build()
        );
    }

    public static void serviceMenuMessage(MessageSender messageSender, Message message){
        messageSender.sendMessage(
                SendMessage.builder()
                        .text("Редагування послуг")
                        .chatId(String.valueOf(message.getChatId()))
                        .replyMarkup(ReplyKeyboardMarkup.builder()
                                .oneTimeKeyboard(true)
                                .resizeKeyboard(true)
                                .keyboardRow(new KeyboardRow(){{
                                    add(KeyboardButton.builder()
                                            .text("Список послуг")
                                            .build());
                                }})
                                .keyboardRow(new KeyboardRow() {{
                                    add(KeyboardButton.builder()
                                            .text("Редагувати послугу")
                                            .build());
                                    add(KeyboardButton.builder()
                                            .text("Видалити послугу")
                                            .build());
                                }})
                                .keyboardRow(new KeyboardRow(){{
                                    add(KeyboardButton.builder()
                                            .text("На головну")
                                            .build());

                                }})
                                .build())
                        .build()
        );
    }

    public static void startServiceMenuMessage(MessageSender messageSender, Message message){
        messageSender.sendMessage(SendMessage.builder()
                .text("Виберіть послугу, яку надаєте:")
                .chatId(String.valueOf(message.getChatId()))
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
                        }})
                        .keyboardRow(new KeyboardRow(){{
                            add(KeyboardButton.builder()
                                    .text("На головну")
                                    .build());

                        }})
                        .build())
                .build());
    }



}
