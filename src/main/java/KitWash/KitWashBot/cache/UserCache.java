package KitWash.KitWashBot.cache;

import KitWash.KitWashBot.domain.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserCache implements Cache<User> {
    private final Map<Long, User> users;

    public UserCache() {
        this.users = new HashMap<>();
    }

    @Override
    public void add(User botUser) {
        if (botUser.getId() != null) {
            users.put(botUser.getId(), botUser);
        }
    }

    @Override
    public void remove(User botUser) {
        users.remove(botUser.getId());
    }

    @Override
    public User findBy(Long id) {
        return users.get(id);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }
}
