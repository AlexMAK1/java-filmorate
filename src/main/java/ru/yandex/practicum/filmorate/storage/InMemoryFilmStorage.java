package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.service.FilmIdGenerator;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage{

    private final LocalDate EARLIEST_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private final Map<Long, Film> films = new HashMap<>();
    private final FilmIdGenerator filmIdGenerator;

    public Map<Long, Film> getFilms() {
        return films;
    }

    @Autowired
    public InMemoryFilmStorage(FilmIdGenerator filmIdGenerator) {
        this.filmIdGenerator = filmIdGenerator;
    }

    public Film getFilm(long id) {
        if (id < 0) {
            log.error("Ошибка, валидация не пройдена. Id не может быть отрицательным: {}", id);
            throw new NotFoundException("Ошибка, валидация не пройдена. Id не может быть отрицательным.");
        }
        return films.get(id);
    }

    @Override
    public Film create(Film film) {
        validation(film);
        long newId = filmIdGenerator.generate();
        film.setId(newId);
        films.put(newId, film);
        return film;
    }

    @Override
    public void delete(int id) {

    }

    @Override
    public Film update(Film film) {
        if (film.getId() < 0) {
            log.error("Ошибка, валидация не пройдена. Id не может быть отрицательным: {}", film.getId());
            throw new NotFoundException("Ошибка, валидация не пройдена. Id не может быть отрицательным.");
        }
        for (Film oldFilm : films.values()) {
            if (oldFilm.getId() == film.getId()) {
                oldFilm.setName(film.getName());
                oldFilm.setDescription(film.getDescription());
                oldFilm.setReleaseDate(film.getReleaseDate());
                oldFilm.setDuration(film.getDuration());
                oldFilm.setLikes(film.getLikes());
                log.info("Обновляем фильм: {}", oldFilm);
                return oldFilm;
            }
        }
        log.info("Добовляем новый фильм: {}", film);
        return film;
    }

    private void validation(Film film) {
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
