package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.*;

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
        return inMemoryFilmStorage.getFilm(id);
    }

    public void deleteLike(long id, long userId) {
        Film film = findFilmById(id);
        log.info("Удаляем лайк у фильма {}", id);
        film.getLikes().remove(userId);
    }

    public List<Film> getPopularFilms(long count) {
        List<Film> popularFilms = new ArrayList<>((Collection<? extends Film>) Comparator.comparing(Film::getSize));
        if (count == Integer.parseInt(null)) {
            List<Film> filmList = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                filmList.add(popularFilms.get(i));
            }
            return filmList;
        }
        List<Film> countFilmList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            countFilmList.add(popularFilms.get(i));
        }
        return countFilmList;
    }
}
