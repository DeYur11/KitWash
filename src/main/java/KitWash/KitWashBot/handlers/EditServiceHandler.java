package KitWash.KitWashBot.handlers;

import KitWash.KitWashBot.cache.Cache;
import KitWash.KitWashBot.domain.BotUser;
import KitWash.KitWashBot.domain.GeneralStatus;
import KitWash.KitWashBot.domain.ServiceEditStatus;
import KitWash.KitWashBot.messageSender.MessageSender;
import KitWash.KitWashBot.model.Database;
import KitWash.KitWashBot.model.Service;
import KitWash.KitWashBot.model.ServiceType;
import KitWash.KitWashBot.model.Worker;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

@Component
public class EditServiceHandler {

    private final Database database;
    private final MessageSender messageSender;
    private final Cache<BotUser> cache;
    Vector<Worker> freeWorkersVector;
    Vector<Worker> busyWorkersVector;

    public EditServiceHandler(Database database, MessageSender messageSender, Cache<BotUser> cache) {
        this.database = database;
        this.messageSender = messageSender;
        this.cache = cache;
    }

    private void outServicesList(Message message)throws Exception{

        BotUser botUser = cache.findBy(message.getChatId());
        Vector<Service> servicesList = database.getTotalServices();
        String MessageBody= "";
        if (servicesList.size() != 0) {

            MessageBody = "Список наданих послуг:\n";
            for (int i = 0; i < servicesList.size(); i++) {
                MessageBody  = MessageBody.concat((i + 1) + ". " + servicesList.get(i).outString());
            }
        }
        else{
            messageSender.sendMessage(SendMessage.builder()
                    .text("Cписок пустий, редагування неможливе")
                    .chatId(String.valueOf(botUser.getTelegramID()))
                    .build());
            botUser.setGeneralStatus(GeneralStatus.HOME_PAGE);
            UserInputHandler.mainMenuMessage(messageSender, message);
            throw new Exception();
        }
        messageSender.sendMessage(SendMessage.builder()
                .text(MessageBody)
                .chatId(String.valueOf(botUser.getTelegramID()))
                .build());
    }
    private void workersListToAdd(Message message){
        BotUser botUser = cache.findBy(message.getChatId());
        Vector<Worker> workersVector = database.getWorkers();
        freeWorkersVector = new Vector<>();
        String MessageBody= "";

        if (workersVector.size() != botUser.getService().getWorkers().size()) {

            MessageBody = "Оберіть працівника:\n";
            for (int i = 0; i < workersVector.size(); i++) {
                if(!botUser.getService().getWorkers().contains(workersVector.get(i))){
                    freeWorkersVector.add(workersVector.get(i));
                    MessageBody  = MessageBody.concat(freeWorkersVector.size() + ". " + workersVector.get(i).getSurname() + " " + workersVector.get(i).getName() + '\n');
                }
            }
        }
        else{
            messageSender.sendMessage(SendMessage.builder()
                    .text("Задіяні всі працівники")
                    .chatId(String.valueOf(botUser.getTelegramID()))
                    .build());
            botUser.setGeneralStatus(GeneralStatus.HOME_PAGE);
            UserInputHandler.mainMenuMessage(messageSender, message);
        }
        messageSender.sendMessage(SendMessage.builder()
                .text(MessageBody)
                .chatId(String.valueOf(botUser.getTelegramID()))
                .build());
        botUser.setServiceEditStatus(ServiceEditStatus.ADD_WORKERS);
    }

    private void workersListToDelete(Message message){
        BotUser botUser = cache.findBy(message.getChatId());
        Vector<Worker> workersVector = database.getWorkers();
        busyWorkersVector = new Vector<>();
        String MessageBody= "";

        if (workersVector.size() != 1) {

            MessageBody = "Оберіть працівника:\n";
            for (int i = 0; i < workersVector.size(); i++) {
                if(botUser.getService().getWorkers().contains(workersVector.get(i))){
                    busyWorkersVector.add(workersVector.get(i));
                    MessageBody  = MessageBody.concat((i + 1) + ". " + workersVector.get(i).getSurname() + " " + workersVector.get(i).getName() + '\n');
                }
            }
        }
        else{
            messageSender.sendMessage(SendMessage.builder()
                    .text("Не можете видалити останього працівника!")
                    .chatId(String.valueOf(botUser.getTelegramID()))
                    .build());
            botUser.setGeneralStatus(GeneralStatus.HOME_PAGE);
            UserInputHandler.mainMenuMessage(messageSender, message);
        }
        messageSender.sendMessage(SendMessage.builder()
                .text(MessageBody)
                .chatId(String.valueOf(botUser.getTelegramID()))
                .build());
        botUser.setServiceEditStatus(ServiceEditStatus.DELETE_WORKERS);
    }


    private void addWorker(Message message){

        try {
            BotUser botUser = cache.findBy(message.getChatId());
            int index = Integer.parseInt(message.getText());

            botUser.getService().addWorker(freeWorkersVector.get(index - 1));
            freeWorkersVector.get(index - 1).addCompletedService(botUser.getService());

            botUser.setGeneralStatus(GeneralStatus.HOME_PAGE);
            UserInputHandler.mainMenuMessage(messageSender, message);
        }
        catch (Exception exc){
            exc.printStackTrace();
        }

    }
    private void deleteWorker(Message message) {
        BotUser botUser = cache.findBy(message.getChatId());
        int index = Integer.parseInt(message.getText());

        botUser.getService().deleteWorker(busyWorkersVector.get(index - 1));
        busyWorkersVector.get(index - 1).removeService(botUser.getService());

        botUser.setGeneralStatus(GeneralStatus.HOME_PAGE);
        UserInputHandler.mainMenuMessage(messageSender, message);
    }
    public void chooseService(Message message){
        BotUser botUser = cache.findBy(message.getChatId());

        try {
            outServicesList(message); // Виводимо працівників
        } catch (Exception e) {
            return;
        }
        messageSender.sendMessage(SendMessage.builder()
                .text("Виберіть послугу зі списку, вписавши її номер: ")
                .chatId(String.valueOf(botUser.getTelegramID()))
                .build());

        botUser.setServiceEditStatus(ServiceEditStatus.NUMBER);

    }

    private void chooseField(Message message){
        BotUser botUser = cache.findBy(message.getChatId());
        int index = Integer.parseInt(message.getText());
        try {
            botUser.setService(database.getTotalServices().elementAt(index-1));
        }catch (IndexOutOfBoundsException e){

            messageSender.sendMessage(SendMessage.builder()
                    .text("Невірний номер послуги")
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
                                    .text("Тип послуги")
                                    .build());
                            add(KeyboardButton.builder()
                                    .text("Час початку послуги")
                                    .build());
                            add(KeyboardButton.builder()
                                    .text("Час закінчення послуги")
                                    .build());

                        }})
                        .keyboardRow(new KeyboardRow() {{
                             add(KeyboardButton.builder()
                                     .text("Додати працівника")
                                     .build());
                             add(KeyboardButton.builder()
                                     .text("Видалити працівника")
                                     .build());
                        }}).build())

                .build());
        botUser.setServiceEditStatus(ServiceEditStatus.INPUT_FIELDS);
    }
    private void enterServiceType(Message message){
        BotUser botUser = cache.findBy(message.getChatId());

        messageSender.sendMessage(SendMessage.builder()
                .text("Виберіть тип послуги для заміни: ")
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
                        .build())
                .build());
        botUser.setServiceEditStatus(ServiceEditStatus.CHOOSE_TYPE);
    }
    private void enterStartTime(Message message){
        BotUser botUser = cache.findBy(message.getChatId());
        messageSender.sendMessage(SendMessage.builder()
                .text("Введіть час початку послуги для заміни(HH:mm): ")
                .chatId(String.valueOf(botUser.getTelegramID()))
                .build());
        botUser.setServiceEditStatus(ServiceEditStatus.START_TIME);
    }
    private void enterEndTime(Message message){
        BotUser botUser = cache.findBy(message.getChatId());

        messageSender.sendMessage(SendMessage.builder()
                .text("Введіть час закінчення послуги для заміни(HH:mm): ")
                .chatId(String.valueOf(botUser.getTelegramID()))
                .build());
        botUser.setServiceEditStatus(ServiceEditStatus.END_TIME);
    }

    private void handleFieldInput(Message message){
        switch (message.getText()){
            case "Тип послуги" -> enterServiceType(message);
            case "Час закінчення послуги" -> enterEndTime(message);
            case "Час початку послуги" -> enterStartTime(message);
            case "Додати працівника" -> workersListToAdd(message);
            case "Видалити працівника" -> workersListToDelete(message);
            default -> {  System.out.println("Error");
            }
        }
    }

    public  void setServiceType(Message message){
        BotUser botUser = cache.findBy(message.getChatId());
        String serviceName = message.getText();

        Integer integer = database.getTotalServices().indexOf(botUser.getService());

        switch (serviceName){
            case "Мийка кузова" -> database.getTotalServices().get(integer).setServiceType(ServiceType.BODYWASH);
            case "Мийка кузова і салону" -> database.getTotalServices().get(integer).setServiceType(ServiceType.INTERIORBODYWASH);
            case "Хімчистка" -> database.getTotalServices().get(integer).setServiceType(ServiceType.DRYCLEANING);
        }
        botUser.setGeneralStatus(GeneralStatus.HOME_PAGE);
        UserInputHandler.mainMenuMessage(messageSender, message);
    }


    public  void editStartTime(Message message){
        BotUser botUser = cache.findBy(message.getChatId());
        String startTime = message.getText();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");


        Integer integer = database.getTotalServices().indexOf(botUser.getService());

        database.getTotalServices().get(integer).setStartDate(LocalTime.parse(startTime, dtf));

        botUser.setGeneralStatus(GeneralStatus.HOME_PAGE);
        UserInputHandler.mainMenuMessage(messageSender, message);
    }
    public  void editEndTime(Message message){
        BotUser botUser = cache.findBy(message.getChatId());
        String endTime = message.getText();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");

        Integer integer = database.getTotalServices().indexOf(botUser.getService());

        database.getTotalServices().get(integer).setEndDate(LocalTime.parse(endTime, dtf));

        botUser.setGeneralStatus(GeneralStatus.HOME_PAGE);
        UserInputHandler.mainMenuMessage(messageSender, message);
    }
    public void generalHandler(Message message){
        BotUser botUser = cache.findBy(message.getChatId());
        Vector<Worker> workers = database.getWorkers();

        switch (botUser.getServiceEditStatus()){
            case NONE -> chooseService(message);
            case NUMBER -> chooseField(message);
            case INPUT_FIELDS -> handleFieldInput(message);
            case CHOOSE_TYPE -> setServiceType(message);
            case START_TIME -> editStartTime(message);
            case END_TIME -> editEndTime(message);
            case ADD_WORKERS -> addWorker(message);
            case DELETE_WORKERS -> deleteWorker(message);
        }
    }
}
