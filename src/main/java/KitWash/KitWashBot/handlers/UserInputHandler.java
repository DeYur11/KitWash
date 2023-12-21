package KitWash.KitWashBot.handlers;

import KitWash.KitWashBot.cache.Cache;
import KitWash.KitWashBot.domain.*;
import KitWash.KitWashBot.messageSender.MessageSender;
import KitWash.KitWashBot.model.Database;
import KitWash.KitWashBot.model.Worker;
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

    private final ServiceInputHandler serviceInputHandler;
    private final Database database;
    private final ManageWorkerHandler manageWorkerHandler;
    private final ManageServiceHandler manageServiceHandler;

    //конструктор класу UserInputHandler
    public UserInputHandler(MessageSender messageSender, Database database, AdminInputHandler adminInputHandler, Cache<BotUser> cache, ServiceInputHandler serviceInputHandler, EditWorkerHandler editWorkerHandler, ManageWorkerHandler manageWorkerHandler,ManageServiceHandler manageServiceHandler){
        this.messageSender = messageSender;
        this.database = database;
        this.cache = cache;
        this.serviceInputHandler = serviceInputHandler;
        this.manageWorkerHandler = manageWorkerHandler;
        this.manageServiceHandler = manageServiceHandler;


        BotUser Yura = new BotUser(708874243L);
        Yura.setGeneralStatus(GeneralStatus.START_PAGE);
        cache.add(Yura);

        BotUser Ivan = new BotUser(343523935L);
        Ivan.setGeneralStatus(GeneralStatus.START_PAGE);
        cache.add(Ivan);

        BotUser Anton = new BotUser(623448844L);
        Anton.setGeneralStatus(GeneralStatus.START_PAGE);
        cache.add(Anton);

        try{
            database.addWorker(new Worker("Антон", "Скаковський", 623448844L));
            database.addWorker(new Worker("Юрій", "Дебеляк", 708874243L));
            database.addWorker(new Worker("Іван", "Легеза", 343523935L));
        }catch (Exception exc){
            exc.printStackTrace();
        }
    }

    //функція обробки вводу користувача при роботі з ботом
    public void choose(Message message){
        BotUser botUser = cache.findBy(message.getChatId());
        if(botUser !=null){
            switch (botUser.getGeneralStatus()){
                case START_PAGE:
                    mainMenuMessage(messageSender, message);
                    botUser.setGeneralStatus(GeneralStatus.HOME_PAGE);
                    break;
                case HOME_PAGE:
                    switch (message.getText()){
                        case "Розпочати послугу":
                            botUser.setGeneralStatus(GeneralStatus.WORKING);
                            botUser.setWorkStatus(WorkStatus.INPUT_CATEGORY);
                            startServiceMenuMessage(messageSender, message);
                            break;
                        case "Управління працівниками":
                            botUser.setGeneralStatus(GeneralStatus.MANAGING_WORKERS);
                            workerMenuMessage(messageSender, message);
                            break;
                        case "Управління послугами":
                            botUser.setGeneralStatus(GeneralStatus.MANAGING_SERVICES);
                            serviceMenuMessage(messageSender, message);
                            break;
                    }
                    break;
                case WORKING:
                    serviceInputHandler.choose(message);
                    break;
                case MANAGING_WORKERS:
                    manageWorkerHandler.generalHandler(message);
                    break;
                case MANAGING_SERVICES:
                    manageServiceHandler.generalHandler(message);
                    break;
            }
        }else if (message.hasText()){
            var sendMessage = SendMessage.builder()
                    .text("Зачекайте поки адміністратор додасть вас.\n" +
                            "Ваш telegram-ID: " + message.getChatId())

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

    public void workerMenuMessage(MessageSender messageSender, Message message){
        BotUser botUser = cache.findBy(message.getChatId());
        botUser.setManageStatus(ManageStatus.NONE);

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

    public void serviceMenuMessage(MessageSender messageSender, Message message){
        BotUser botUser = cache.findBy(message.getChatId());
        botUser.setManageStatus(ManageStatus.NONE);

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
