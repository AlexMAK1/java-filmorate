package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserIdGenerator;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Component
@RequiredArgsConstructor
class UserControllerTest {

    private final UserIdGenerator userIdGenerator = new UserIdGenerator();
    private final InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage(userIdGenerator);
    private final UserService userService = new UserService(inMemoryUserStorage);

    UserController userController = new UserController(userService);

    @Test
    void findAll() {
        User user = new User();
        user.setEmail("mail@mail.ru");
        user.setLogin("dolore");
        user.setName("Nick Name");
        user.setBirthday(LocalDate.of(1946, 8, 20));
        user.setFriends(Set.of(1L, 2L, 3L));
        userController.create(user);
        assertEquals(1, userController.findAll().size(), "Коллекция пуста");
    }

    @Test
    void create() {
        User user = new User();
        user.setEmail("mail@mail.ru");
        user.setLogin("dolore");
        user.setName("Nick Name");
        user.setBirthday(LocalDate.of(1946, 8, 20));
        user.setFriends(Set.of(1L, 2L, 3L));
        userController.create(user);
        User testUser = new User();
        testUser.setEmail("mail@mail.ru");
        testUser.setLogin("dolore");
        testUser.setName("Nick Name");
        testUser.setBirthday(LocalDate.of(1946, 8, 20));
        testUser.setFriends(Set.of(1L, 2L, 3L));
        testUser.setId(user.getId());
        assertEquals(testUser, inMemoryUserStorage.getUser(user.getId()), "Пользователь не добавлен");
    }

    @Test
    void saveUser() {
        User user = new User();
        user.setEmail("mail@mail.ru");
        user.setLogin("dolore");
        user.setName("Nick Name");
        user.setBirthday(LocalDate.of(1946, 8, 20));
        user.setFriends(Set.of(1L, 2L, 3L));
        userController.create(user);
        User user2 = new User();
        user.setEmail("yandex@mail.ru");
        user.setLogin("smozy");
        user.setName("Intel");
        user.setBirthday(LocalDate.of(2001, 5, 15));
        user.setFriends(Set.of(5L, 9L, 6L));
        user2.setId(user.getId());
        userController.saveUser(user2);
        User testUser = new User();
        user2.setEmail("yandex@mail.ru");
        user2.setLogin("smozy");
        user2.setName("Intel");
        user2.setBirthday(LocalDate.of(2001, 5, 15));
        user2.setFriends(Set.of(5L, 9L, 6L));
        testUser.setId(user2.getId());
        assertEquals(testUser, inMemoryUserStorage.getUser(user2.getId()), "Пользователь не добавлен");
    }
}
