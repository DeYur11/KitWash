package KitWash.KitWashBot.cache;

import KitWash.KitWashBot.domain.BotUser;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserCache implements Cache<BotUser> {
    private final Map<Long, BotUser> users;

    public UserCache() {
        this.users = new HashMap<>();
    }

    @Override
    public void add(BotUser botUser) {
        if (botUser.getTelegramID() != null) {
            users.put(botUser.getTelegramID(), botUser);
        }
    }

    @Override
    public void remove(BotUser botUser) {
        users.remove(botUser.getTelegramID());
    }

    @Override
    public BotUser findBy(Long id) {
        return users.get(id);
    }

    @Override
    public List<BotUser> getAll() {
        return new ArrayList<>(users.values());
    }
}
