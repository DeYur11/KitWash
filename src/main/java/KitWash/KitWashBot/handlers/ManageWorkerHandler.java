package KitWash.KitWashBot.handlers;

import KitWash.KitWashBot.cache.Cache;
import KitWash.KitWashBot.domain.BotUser;
import KitWash.KitWashBot.messageSender.MessageSender;
import KitWash.KitWashBot.model.Database;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class ManageWorkerHandler {
    private final Database database;
    private final MessageSender messageSender;
    private final EditWorkerHandler editWorkerHandler;
    private final Cache<BotUser> cache;

    public ManageWorkerHandler(Database database, MessageSender messageSender, EditWorkerHandler editWorkerHandler, Cache<BotUser> cache) {
        this.database = database;
        this.messageSender = messageSender;
        this.editWorkerHandler = editWorkerHandler;
        this.cache = cache;
    }

    public void generalHandler(Message message){

    }
}