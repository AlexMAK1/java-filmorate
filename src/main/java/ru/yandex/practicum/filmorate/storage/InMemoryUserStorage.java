package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserIdGenerator;
import ru.yandex.practicum.filmorate.service.UserValidationService;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage{

    private final Map<Long, User> users = new HashMap<>();
    private final UserIdGenerator userIdGenerator;
    private final UserValidationService userValidationService;

    @Autowired
    public InMemoryUserStorage(UserIdGenerator userIdGenerator, UserValidationService userValidationService) {
        this.userIdGenerator = userIdGenerator;
        this.userValidationService = userValidationService;
    }

    @Override
    public Map<Long, User> getUsers() {
        return new HashMap<>(users);
    }

    @Override
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
        userValidationService.validation(user);
        long newId = userIdGenerator.generate();
        user.setId(newId);
        log.info("Сохраняем нового пользователя: {}", user);
        users.put(newId, user);
        return user;
    }

    @Override
    public User update(User user) {
        if (user.getId() < 0) {
            log.error("Ошибка, валидация не пройдена. Id не может быть отрицательным: {}", user.getId());
            throw new NotFoundException("Ошибка, валидация не пройдена. Id не может быть отрицательным.");
        }
        for (User oldUser : users.values()) {
            if (oldUser.getId() == user.getId()) {
                users.put(user.getId(), user);
                log.info("Обновляем данные пользователя: {}", user);
                return user;
            }
        }
        long newId = userIdGenerator.generate();
        user.setId(newId);
        log.info("Добовляем пользователя: {}", user);
        users.put(newId, user);
        return user;
    }
}
