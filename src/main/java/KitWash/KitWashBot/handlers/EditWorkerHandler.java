package KitWash.KitWashBot.handlers;

import KitWash.KitWashBot.cache.Cache;
import KitWash.KitWashBot.domain.BotUser;
import KitWash.KitWashBot.domain.GeneralStatus;
import KitWash.KitWashBot.domain.WorkerEditStatus;
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
public class EditWorkerHandler {
    private final Database database;
    private final MessageSender messageSender;
    private final Cache<BotUser> cache;

    public EditWorkerHandler(Database database, MessageSender messageSender, Cache<BotUser> cache) {
        this.database = database;
        this.messageSender = messageSender;
        this.cache = cache;
    }

    private void outWorkers(Message message) {
        BotUser botUser = cache.findBy(message.getChatId());
        Vector<Worker> workers = database.getWorkers();
        String MessageBody= "";
        if (workers.size() != 0) {

            MessageBody = "Список працівників:\n";
            for (int i = 0; i < workers.size(); i++) {
                MessageBody  = MessageBody.concat((i + 1) + ". " + workers.get(i).toString());
            }
        }
        else{
            messageSender.sendMessage(SendMessage.builder()
                    .text("Cписок пустий, редагування неможливе")
                    .chatId(String.valueOf(botUser.getTelegramID()))
                    .build());
            botUser.setGeneralStatus(GeneralStatus.HOME_PAGE);
            UserInputHandler.mainMenuMessage(messageSender, message);
            return;
        }
        messageSender.sendMessage(SendMessage.builder()
                .text(MessageBody)
                .chatId(String.valueOf(botUser.getTelegramID()))
                .build());
    }

    public void chooseWorker(Message message){
        BotUser botUser = cache.findBy(message.getChatId());
        Vector<Worker> workers = database.getWorkers();

        try {
            outWorkers(message); // Виводимо працівників
        } catch (Exception e) {
            UserInputHandler.mainMenuMessage(messageSender, message);
            botUser.setGeneralStatus(GeneralStatus.HOME_PAGE);
            return;
        }

        messageSender.sendMessage(SendMessage.builder()
                .text("Виберіть працівника зі списку, вписавши його номер: ")
                .chatId(String.valueOf(botUser.getTelegramID()))
                .build());

        botUser.setWorkerEditStatus(WorkerEditStatus.NUMBER);
    }
    private void chooseField(Message message){
        BotUser botUser = cache.findBy(message.getChatId());
        int index = Integer.parseInt(message.getText());
        try {
            botUser.setWorker(database.getWorkers().elementAt(index-1));
        }catch (IndexOutOfBoundsException e){

            messageSender.sendMessage(SendMessage.builder()
                    .text("Невірний номер працівника")
                    .chatId(String.valueOf(botUser.getTelegramID()))
                    .build());
            botUser.setGeneralStatus(GeneralStatus.HOME_PAGE);
            UserInputHandler.mainMenuMessage(messageSender, message);
            return;
        }

        messageSender.sendMessage(SendMessage.builder()
                .text("Виберіть поле для редагування: \n")
                .chatId(message.getChatId())
                .replyMarkup(ReplyKeyboardMarkup.builder()
                        .oneTimeKeyboard(true)
                        .resizeKeyboard(true)
                        .keyboardRow(new KeyboardRow() {{
                            add(KeyboardButton.builder()
                                    .text("Ім'я")
                                    .build());
                            add(KeyboardButton.builder()
                                    .text("Прізвище")
                                    .build());
                            add(KeyboardButton.builder()
                                    .text("Телеграм ID")
                                    .build());
                        }}).build())
                .build());
        botUser.setWorkerEditStatus(WorkerEditStatus.INPUT_FIELDS);
    }

    private void enterSurnameToChange(Message message){
        BotUser botUser = cache.findBy(message.getChatId());

        messageSender.sendMessage(SendMessage.builder()
                .text("Введіть прізвище для заміни: ")
                .chatId(String.valueOf(botUser.getTelegramID()))
                .build());
        botUser.setWorkerEditStatus(WorkerEditStatus.SURNAME);
    }
    private void enterNameToChange(Message message){
        BotUser botUser = cache.findBy(message.getChatId());
        messageSender.sendMessage(SendMessage.builder()
                .text("Введіть ім'я для заміни: ")
                .chatId(String.valueOf(botUser.getTelegramID()))
                .build());
        botUser.setWorkerEditStatus(WorkerEditStatus.NAME);
    }
    private void enterTelegramIDToChange(Message message){
        BotUser botUser = cache.findBy(message.getChatId());

        messageSender.sendMessage(SendMessage.builder()
                .text("Введіть TelegramID для заміни: ")
                .chatId(String.valueOf(botUser.getTelegramID()))
                .build());
        botUser.setWorkerEditStatus(WorkerEditStatus.TELEGRAM_ID);
    }
    private void handleFieldInput(Message message){
        switch (message.getText()){
            case "Прізвище" -> enterSurnameToChange(message);
            case "Ім'я" -> enterNameToChange(message);
            case "Телеграм ID" -> enterTelegramIDToChange(message);
            default -> {
                System.out.println("Error");
                return;
            }
        }
    }

    private void editName(Message message){
        BotUser botUser = cache.findBy(message.getChatId());
        String newName = message.getText();

        Integer integer = database.getWorkers().indexOf(botUser.getWorker());

        database.getWorkers().get(integer).setName(newName);
        botUser.setGeneralStatus(GeneralStatus.HOME_PAGE);
        UserInputHandler.mainMenuMessage(messageSender, message);
    }
    private void editSurname(Message message){
        BotUser botUser = cache.findBy(message.getChatId());
        String newSurname = message.getText();

        Integer integer = database.getWorkers().indexOf(botUser.getWorker());

        database.getWorkers().get(integer).setSurname(newSurname);
        botUser.setGeneralStatus(GeneralStatus.HOME_PAGE);
        UserInputHandler.mainMenuMessage(messageSender, message);
    }
    private void editTelegramId(Message message){
        BotUser botUser = cache.findBy(message.getChatId());
        Long newTelegramId = Long.valueOf(Integer.valueOf(message.getText()));

        Integer integer = database.getWorkers().indexOf(botUser.getWorker());

        BotUser toChange = cache.findBy(database.getWorkers().get(integer).getTelegramId());
        BotUser newUser = new BotUser(newTelegramId, toChange.getGeneralStatus());

        cache.replaceBotUser(toChange, newUser);
        database.getWorkers().get(integer).setTelegramId(newTelegramId);


        botUser.setGeneralStatus(GeneralStatus.HOME_PAGE);
        UserInputHandler.mainMenuMessage(messageSender, message);
    }

    public void generalHandler(Message message){
        BotUser botUser = cache.findBy(message.getChatId());
        Vector<Worker> workers = database.getWorkers();

        switch (botUser.getWorkerEditStatus()){
            case NONE -> chooseWorker(message);
            case NUMBER -> chooseField(message);
            case INPUT_FIELDS -> handleFieldInput(message);
            case NAME -> editName(message);
            case SURNAME -> editSurname(message);
            case TELEGRAM_ID -> editTelegramId(message);
        }
    }




}
