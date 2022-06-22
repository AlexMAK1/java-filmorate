package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    private final InMemoryFilmStorage inMemoryFilmStorage;

    public InMemoryFilmStorage getInMemoryFilmStorage() {
        return inMemoryFilmStorage;
    }

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    public void addLike(long id, long userId) {
        Film film = findFilmById(id);
        log.info("Добавляем лайк фильму {}", id);
        film.getLikes().add(userId);
    }

    public Film findFilmById(Long id) {
        if (!inMemoryFilmStorage.getFilms().containsKey(id)) {
            log.error("Ошибка, такого фидьиа нет: {}", id);
            throw new NotFoundException("Ошибка, такого фильма нет");
        }
        log.info("Находим фильм {}", id);
        return inMemoryFilmStorage.getFilm(id);
    }

    public void deleteLike(long id, long userId) {
        if (id < 0 || userId < 0) {
            log.error("Ошибка, валидация не пройдена. Id не может быть отрицательным: {}", userId);
            throw new NotFoundException("Ошибка, валидация не пройдена. Id не может быть отрицательным.");
        }
        Film film = findFilmById(id);
        log.info("Удаляем лайк у фильма {}", id);
        film.getLikes().remove(userId);
    }

    public List<Film> getCountPopularFilms(Long count) {
        List<Film> popularFilms = new ArrayList<>(inMemoryFilmStorage.getFilms().values());
        return popularFilms
                .stream()
                .sorted(this::compare)
                .limit(count)
                .collect(Collectors.toList());
    }

    private int compare(Film a, Film b) {
        return b.getLikes().size() - a.getLikes().size();
    }
}
