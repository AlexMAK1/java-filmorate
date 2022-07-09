package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserIdGenerator;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.UserValidationService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Component
@RequiredArgsConstructor
class UserControllerTest {

    private final UserIdGenerator userIdGenerator = new UserIdGenerator();
    private final UserValidationService userValidationService = new UserValidationService();
    private final InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage(userIdGenerator, userValidationService);
    private final UserService userService = new UserService(inMemoryUserStorage);

    UserController userController = new UserController(userService);

    @Test
    void findAll() {
        User user = new User();
        user.setEmail("mail@mail.ru");
        user.setLogin("dolore");
        user.setName("Nick Name");
        user.setBirthday(LocalDate.of(1946, 8, 20));
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
        userController.create(user);
        User testUser = new User();
        testUser.setEmail("mail@mail.ru");
        testUser.setLogin("dolore");
        testUser.setName("Nick Name");
        testUser.setBirthday(LocalDate.of(1946, 8, 20));
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
        userController.create(user);
        User user2 = new User();
        user2.setEmail("yandex@mail.ru");
        user2.setLogin("smozy");
        user2.setName("Intel");
        user2.setBirthday(LocalDate.of(2001, 5, 15));
        user2.setId(user.getId());
        userController.saveUser(user2);
        User testUser = new User();
        testUser.setEmail("yandex@mail.ru");
        testUser.setLogin("smozy");
        testUser.setName("Intel");
        testUser.setBirthday(LocalDate.of(2001, 5, 15));
        testUser.setId(user2.getId());
        assertEquals(testUser, inMemoryUserStorage.getUser(user2.getId()), "Пользователь не добавлен");
    }
}
