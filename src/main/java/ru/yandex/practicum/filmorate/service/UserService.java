package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.HashSet;
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
        otherUser.getFriends().remove(id);
    }

    public Set<Long> getFriends(long id) {
        return inMemoryUserStorage.getUser(id).getFriends();
    }

    public Set<Long> getCommonFriends(long id, long otherId) {
        Set<Long> commonFriends = new HashSet<>();
        for (long i : inMemoryUserStorage.getUser(id).getFriends()) {
            for (long t : inMemoryUserStorage.getUser(otherId).getFriends()) {
                if (i == t) {
                    commonFriends.add(i);
                }
            }
        }
        if (commonFriends.isEmpty()) {
            System.out.println("Список обших друзей пуст");
        }
        return commonFriends;
    }

}
