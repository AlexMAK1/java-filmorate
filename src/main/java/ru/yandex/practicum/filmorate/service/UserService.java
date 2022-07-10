package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class UserService {

    @Autowired
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Map<Long, User> getUsers() {
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
        User user = findUserById(id);
        User otherUser = findUserById(friendId);
        user.getFriends().add(friendId);
        otherUser.getFriends().add(id);
        log.info("Добавление в друзья. Юзер 1: {}, Юзер 2: {}", id, friendId);
    }

    public User findUserById(Long id) {
        if (!userStorage.getUsers().containsKey(id)) {
            log.error("Ошибка, такого пользователя нет: {}", id);
            throw new NotFoundException("Ошибка, такого пользователя нет");
        }
        return userStorage.getUser(id);
    }

    public void deleteFriend(long id, long friendId) {
        User user = findUserById(id);
        User otherUser = findUserById(friendId);
        user.getFriends().remove(friendId);
        log.info("Удаляем из списка друзей друг у друга. Юзер 1: {}, Юзер 2: {}", id, friendId);
        otherUser.getFriends().remove(id);
    }

    public List<User> getFriends(long id) {
        List<User> friends = new ArrayList<>();
        User user = userStorage.getUser(id);
        Set<Long> friendsId;
        friendsId = user.getFriends();
        for (long i : friendsId) {
            friends.add(userStorage.getUser(i));
        }
        log.info("Находим друзей Юзера: {}, список друзей :{}", id, friends);
        return friends;
    }

    public List<User> getCommonFriends(long id, long otherId) {
        findUserById(id);
        findUserById(otherId);
        List<User> commonFriends = new ArrayList<>();
        Set<Long> userFriends = userStorage.getUser(id).getFriends();
        for (long i : userStorage.getUser(otherId).getFriends()) {
            if (userFriends.contains(i)) {
                commonFriends.add(userStorage.getUser(i));
            }
        }
        log.info("Находим список общих друзей. Юзера 1: {}, Юзера 2: {}", id, otherId);
        return commonFriends;
    }
}

