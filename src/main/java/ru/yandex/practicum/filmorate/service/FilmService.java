package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.*;

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
        inMemoryFilmStorage.getFilm(id).getLikes().add(userId);
        inMemoryFilmStorage.getFilm(userId).getLikes().add(id);
    }

    public void deleteLike(long id, long userId) {
        inMemoryFilmStorage.getFilm(id).getLikes().remove(userId);
        inMemoryFilmStorage.getFilm(userId).getLikes().remove(id);
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
