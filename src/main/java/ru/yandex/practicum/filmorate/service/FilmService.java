package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film getById(long id) {
        return filmStorage.getFilm(id);
    }

    public List <Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
    return filmStorage.update(film);
    }

    public void addLike(long id, long userId) {
        Film film = findFilmById(id);
        log.info("Добавляем лайк фильму {}", id);

    }

    public Film findFilmById(Long id) {
        if (filmStorage.getFilms().isEmpty()) {
            log.error("Ошибка, такого фильма нет: {}", id);
            throw new NotFoundException("Ошибка, такого фильма нет");
        }
        log.info("Находим фильм {}", id);
        return filmStorage.getFilm(id);
    }

    public void deleteLike(long id, long userId) {
        if (id < 0 || userId < 0) {
            log.error("Ошибка, валидация не пройдена. Id не может быть отрицательным: {}", userId);
            throw new NotFoundException("Ошибка, валидация не пройдена. Id не может быть отрицательным.");
        }
        Film film = findFilmById(id);
        log.info("Удаляем лайк у фильма {}", id);

    }

    public List<Film> getCountPopularFilms(Long count) {
        List<Film> popularFilms = new ArrayList<>(filmStorage.getFilms());
        return popularFilms;
    }
}
