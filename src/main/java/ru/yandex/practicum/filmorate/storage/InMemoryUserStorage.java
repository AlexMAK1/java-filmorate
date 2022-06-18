package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.service.UserIdGenerator;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage{

    private final Map<Long, User> users = new HashMap<>();
    private final UserIdGenerator userIdGenerator;

    @Autowired
    public InMemoryUserStorage(UserIdGenerator userIdGenerator) {
        this.userIdGenerator = userIdGenerator;
    }

    public Map<Long, User> getUsers() {
        return users;
    }

    public User getUser(long id) {
        if (id < 0) {
            log.error("Ошибка, валидация не пройдена. Id не может быть отрицательным: {}", id);
            throw new NotFoundException("Ошибка, валидация не пройдена. Id не может быть отрицательным.");
        }
        return users.get(id);
    }

    @Override
    public User create(User user) {
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        validation(user);
        long newId = userIdGenerator.generate();
        user.setId(newId);
        log.info("Сохраняем нового пользователя: {}", user);
        users.put(newId, user);
        return user;
    }

    @Override
    public void delete(long id) {

    }

    @Override
    public User update(User user) {
        if (user.getId() < 0) {
            log.error("Ошибка, валидация не пройдена. Id не может быть отрицательным: {}", user.getId());
            throw new NotFoundException("Ошибка, валидация не пройдена. Id не может быть отрицательным.");
        }
        for (User oldUser : users.values()) {
            if (oldUser.getId() == user.getId()) {
                oldUser.setEmail(user.getEmail());
                oldUser.setLogin(user.getLogin());
                oldUser.setName(user.getName());
                oldUser.setBirthday(user.getBirthday());
                oldUser.setFriends(user.getFriends());
                log.info("Обновляем данные пользователя: {}", oldUser);
                return oldUser;
            }
        }
        long newId = userIdGenerator.generate();
        user.setId(newId);
        log.info("Добовляем пользователя: {}", user);
        users.put(newId, user);
        return user;
    }

    private void validation(User user) {
        LocalDate currentMoment = LocalDate.now();

        if (!user.getEmail().contains("@") || user.getEmail().isEmpty()) {
            log.error("Ошибка, валидация не пройдена. Электронная почта не может быть пустой и должна содержать символ @: {}", user.getEmail());
            throw new ValidationException("Ошибка, валидация не пройдена. Электронная почта не может быть пустой и должна содержать символ @.");
        }
        if (user.getLogin().isEmpty() && user.getLogin().contains(" ")) {
            log.error("Ошибка, валидация не пройдена. Логин не может быть пустым и содержать пробелы: {}", user.getLogin());
            throw new ValidationException("Ошибка, валидация не пройдена. Логин не может быть пустым и содержать пробелы.");
        }
        if (user.getBirthday().isAfter(currentMoment)) {
            log.error("Ошибка, валидация не пройдена. Дата рождения не может быть в будущем: {}", user.getBirthday());
            throw new ValidationException("Ошибка, валидация не пройдена. Дата рождения не может быть в будущем.");
        }
    }
}
