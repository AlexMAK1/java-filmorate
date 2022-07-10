package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
@Service
public class UserValidationService {

    public void validation(User user) {
        LocalDate currentMoment = LocalDate.now();

        if (!user.getEmail().contains("@") || user.getEmail().isEmpty()) {
            log.error("Ошибка, валидация не пройдена. Электронная почта не может быть пустой и должна содержать символ @: {}", user.getEmail());
            throw new ValidationException("Ошибка, валидация не пройдена. Электронная почта не может быть пустой и должна содержать символ @.");
        }
        if (user.getLogin().isEmpty() && user.getLogin().contains(" ")) {
            log.error("Ошибка, валидация не пройдена. Логин не может быть пустым и содержать пробелы: {}", user.getLogin());
            throw new ValidationException("Ошибка, валидация не пройдена. Логин не может быть пустым и содержать пробелы.");
        }
    }
}
