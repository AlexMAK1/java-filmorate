package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film getFilm(long id);

    Film create(Film film);

    Film update(Film film);

    List<Film> getFilms();

    void setFilmGenre(Film film);

    void updateFilmGenre(Film film);

    void addLike(long id, long userId);

    void deleteLike(long id, long userId);

    List<Film> getPopularFilms(Integer count);
}
