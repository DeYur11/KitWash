package KitWash.KitWashBot.cache;

import KitWash.KitWashBot.domain.BotUser;

import java.util.List;

public interface Cache<T> {
    void add(T t);

    void remove(T t);

    T findBy(Long id);

    List<T> getAll();
    public void replaceBotUser(BotUser oldUser, BotUser newUser);
}