package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

public interface FilmStorage {

   Film create(Film film);

   void delete (int id);

   Film update(Film film);
}
