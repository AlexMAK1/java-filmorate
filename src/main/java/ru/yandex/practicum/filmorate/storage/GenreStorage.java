package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenreStorage {
    Genre getGenre(int id);

    Set<Genre> getFilmGenres(long id);

    List<Genre> getGenres();
}
