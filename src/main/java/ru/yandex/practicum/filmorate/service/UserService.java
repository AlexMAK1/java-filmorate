package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUser(long id) {
        return userStorage.getUser(id);
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public void addFriend(long id, long friendId) {
        log.info("Добавление в друзья. Юзер 1: {}, Юзер 2: {}", id, friendId);
        userStorage.addFriend(id, friendId);
    }

    public User findUserById(Long id) {
        if (userStorage.getUsers().isEmpty()) {
            log.error("Ошибка, такого пользователя нет: {}", id);
            throw new NotFoundException("Ошибка, такого пользователя нет");
        }
        return userStorage.getUser(id);
    }

    public void deleteFriend(long id, long friendId) {
        log.info("Удаляем из списка друзей друг у друга. Юзер 1: {}, Юзер 2: {}", id, friendId);
        userStorage.deleteFriend(id, friendId);
    }

    public List<User> getFriends(long id) {
        log.info("Находим друзей Юзера: {}, список друзей :{}", id, userStorage.getUserFriends(id));
        return userStorage.getUserFriends(id);

    }

    public List<User> getCommonFriends(long id, long otherId) {
        log.info("Находим список общих друзей. Юзера 1: {}, Юзера 2: {}", id, otherId);
        return userStorage.getCommonFriends(id, otherId);
    }
}

