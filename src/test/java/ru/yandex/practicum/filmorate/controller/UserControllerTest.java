package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserIdGenerator;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserControllerTest {

    private final UserIdGenerator userIdGenerator = new UserIdGenerator();
    private final InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage(userIdGenerator);
    private final UserService userService = new UserService(inMemoryUserStorage);

    UserController userController = new UserController(userService);

    @Test
    void findAll() {
        User user = new User("mail@mail.ru", "dolore", "Nick Name", LocalDate.of(1946, 8, 20), Set.of(1L, 2L, 3L));
        userController.create(user);
        assertEquals(1, userController.findAll().size(), "Коллекция пуста");
    }

    @Test
    void create() {
        User user = new User("mail@mail.ru", "dolore", "Nick Name", LocalDate.of(1946, 8, 20), Set.of(1L, 2L, 3L));
        userController.create(user);
        User testUser = new User("mail@mail.ru", "dolore", "Nick Name", LocalDate.of(1946, 8, 20), Set.of(1L, 2L, 3L));
        testUser.setId(user.getId());
        assertEquals(testUser, inMemoryUserStorage.getUser(user.getId()), "Пользователь не добавлен");
    }

    @Test
    void saveUser() {
        User user1 = new User("mail@mail.ru", "dolore", "Nick Name", LocalDate.of(1946, 8, 20), Set.of(1L, 2L, 3L));
        userController.create(user1);
        User user2 = new User("yandex@mail.ru", "smozy", "Intel", LocalDate.of(2001, 5, 15), Set.of(1L, 2L, 3L));
        user2.setId(user1.getId());
        userController.saveUser(user2);
        User testUser = new User("yandex@mail.ru", "smozy", "Intel", LocalDate.of(2001, 5, 15), Set.of(1L, 2L, 3L));
        testUser.setId(user2.getId());
        assertEquals(testUser, inMemoryUserStorage.getUser(user2.getId()), "Пользователь не добавлен");
    }
}
