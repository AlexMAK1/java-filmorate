package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

public interface UserStorage {

    User create(User user);

    void delete (long id);

    User update(User user);
}
