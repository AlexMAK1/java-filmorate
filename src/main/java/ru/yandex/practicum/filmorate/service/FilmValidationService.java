package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Slf4j
@Service
public class FilmValidationService {

    private final LocalDate EARLIEST_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    public void validation(Film film) {
        int len = film.getDescription().length();

        if (film.getName().isEmpty()) {
            log.error("Ошибка, валидация не пройдена. Название фильма не может быть пустым: {}", film.getName());
            throw new ValidationException("Ошибка, валидация не пройдена. Название фильма не может быть пустым.");
        }
        if (len > 200) {
            log.error("Ошибка, валидация не пройдена. Максимальная длина описания должна быть не больше 200 символов: {}", film.getDescription());
            throw new ValidationException("Ошибка, валидация не пройдена. Максимальная длина описания должна быть не больше 200 символов.");
        }
        if (film.getReleaseDate().isBefore(EARLIEST_RELEASE_DATE)) {
            log.error("Ошибка, валидация не пройдена. Дата релиза должна быть не раньше 28 декабря 1895 года: {}", film.getReleaseDate());
            throw new ValidationException("Ошибка, валидация не пройдена. Дата релиза должна быть не раньше 28 декабря 1895 года.");
        }
        if (film.getDuration() < 0) {
            log.error("Ошибка, валидация не пройдена. Продолжительность фильма должна быть положительной: {}", film.getDuration());
            throw new ValidationException("Ошибка, валидация не пройдена. Продолжительность фильма должна быть положительной.");
        }
    }
}
