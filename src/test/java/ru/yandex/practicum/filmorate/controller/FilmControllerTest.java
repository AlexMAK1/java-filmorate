package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmIdGenerator;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FilmControllerTest {

    FilmIdGenerator filmIdGenerator = new FilmIdGenerator();
    InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage(filmIdGenerator);
    FilmService filmService = new FilmService(inMemoryFilmStorage);
    FilmController filmController = new FilmController(filmService);

    @Test
    void findAll(){
        Film film = new Film("nisi eiusmod", "adipisicing", LocalDate.of(1967, 03, 25), 100, Set.of(1L, 2L, 3L));
        filmController.create(film);
        assertEquals(1, filmController.findAll().size(), "Коллекция пуста");
    }

    @Test
    void create(){
        Film film = new Film("nisi eiusmod", "adipisicing", LocalDate.of(1967, 03, 25), 100, Set.of(1L, 2L, 3L));
        Film testFilm = new Film("nisi eiusmod", "adipisicing", LocalDate.of(1967, 03, 25), 100, Set.of(1L, 2L, 3L));
        filmController.create(film);
        testFilm.setId(film.getId());
        assertEquals(testFilm, inMemoryFilmStorage.getFilm(film.getId()), "Фильм не добавлен");
    }

    @Test
    void saveFilm(){
        Film film1 = new Film("nisi eiusmod", "adipisicing", LocalDate.of(1967, 03, 25), 100, Set.of(1L, 2L, 3L));
        filmController.create(film1);
        Film film2 = new Film("terminator", "action", LocalDate.of(1967, 03, 25), 777, Set.of(1L, 2L, 5L));
        film2.setId(film1.getId());
        filmController.saveFilm(film2);
        Film testFilm = new Film("terminator", "action", LocalDate.of(1967, 03, 25), 777, Set.of(1L, 2L, 5L));
        testFilm.setId(film1.getId());
        assertEquals(testFilm, inMemoryFilmStorage.getFilm(film2.getId()), "Фильм не добавлен");
    }
}
