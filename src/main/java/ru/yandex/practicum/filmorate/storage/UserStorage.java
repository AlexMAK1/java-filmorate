package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User create(User user);

    User getUser (long id);

    List<User> getUsers();

    User update(User user);

    void addFriend(long id, long friendId);

    void deleteFriend(long id, long friendId);

    List<User> getCommonFriends(long userId, long otherId);

    List<User> getUserFriends(long userId);
}
