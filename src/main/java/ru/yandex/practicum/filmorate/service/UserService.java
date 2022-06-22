package ru.yandex.practicum.filmorate.service;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class UserService {

    private final InMemoryUserStorage inMemoryUserStorage;

    public InMemoryUserStorage getInMemoryUserStorage() {
        return inMemoryUserStorage;
    }

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public void addFriend(long id, long friendId) {
        User user = findUserById(id);
        User otherUser = findUserById(friendId);
        user.getFriends().add(friendId);
        otherUser.getFriends().add(id);
        log.info("Добавляем в список друзей");
    }

    public User findUserById(Long id) {
        if (!inMemoryUserStorage.getUsers().containsKey(id)) {
            log.error("Ошибка, такого пользователя нет: {}", id);
            throw new NotFoundException("Ошибка, такого пользователя нет");
        }
        return inMemoryUserStorage.getUser(id);
    }

    public void deleteFriend(long id, long friendId) {
        User user = findUserById(id);
        User otherUser = findUserById(friendId);
        user.getFriends().remove(friendId);
        log.info("Удаляем пользователя из списка друзей: {}", id);
        otherUser.getFriends().remove(id);
    }

    public List<User> getFriends(long id) {
        List<User> friends = new ArrayList<>();
        User user = inMemoryUserStorage.getUser(id);
        Set<Long> friendsId;
        friendsId = user.getFriends();
        for (long i : friendsId) {
            friends.add(inMemoryUserStorage.getUser(i));
        }
        log.info("Находим друзей пользователя: {}", friends);
        return friends;
    }

    public List<User> getCommonFriends(long id, long otherId) {
        findUserById(id);
        findUserById(otherId);
        List<User> commonFriends = new ArrayList<>();
        Set<Long> userFriends = inMemoryUserStorage.getUser(id).getFriends();
        for (long i : inMemoryUserStorage.getUser(otherId).getFriends()) {
            if (userFriends.contains(i)) {
                commonFriends.add(inMemoryUserStorage.getUser(i));
            }
        }
        log.info("Находим список общих друзей: {}", commonFriends);
        return commonFriends;
    }
}

