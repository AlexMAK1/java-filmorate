package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmIdGenerator;
import ru.yandex.practicum.filmorate.service.FilmValidationService;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private final FilmIdGenerator filmIdGenerator;
    private final FilmValidationService filmValidationService;

    @Autowired
    public InMemoryFilmStorage(FilmIdGenerator filmIdGenerator, FilmValidationService filmValidationService) {
        this.filmIdGenerator = filmIdGenerator;
        this.filmValidationService = filmValidationService;
    }

    @Override
    public Map<Long, Film> getFilms() {
        return new HashMap<>(films);
    }

    @Override
    public Film getFilm(long id) {
        if (id < 0) {
            log.error("Ошибка, валидация не пройдена. Id не может быть отрицательным: {}", id);
            throw new NotFoundException("Ошибка, валидация не пройдена. Id не может быть отрицательным.");
        }
        return films.get(id);
    }

    @Override
    public Film create(Film film) {

        filmValidationService.validation(film);
        long newId = filmIdGenerator.generate();
        film.setId(newId);
        films.put(newId, film);
        log.info("Сохраняем новый фильм: {}", film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (film.getId() < 0) {
            log.error("Ошибка, валидация не пройдена. Id не может быть отрицательным: {}", film.getId());
            throw new NotFoundException("Ошибка, валидация не пройдена. Id не может быть отрицательным.");
        }
        for (Film oldFilm : films.values()) {
            if (oldFilm.getId() == film.getId()) {
                films.put(film.getId(), film);
                log.info("Обновляем существующий фильм: {}", film);
                return film;
            }
        }
        long newId = filmIdGenerator.generate();
        film.setId(newId);
        films.put(newId, film);
        log.info("Добовляем новый фильм: {}", film);
        return film;
    }
}
