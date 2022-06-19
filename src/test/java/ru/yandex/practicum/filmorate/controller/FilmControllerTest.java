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
        Film film = new Film();
        film.setName("nisi eiusmod");
        film.setDescription("adipisicing");
        film.setReleaseDate(LocalDate.of(1967, 03, 25));
        film.setDuration(100);
        film.setLikes(Set.of(1L, 2L, 3L));
        filmController.create(film);
        assertEquals(1, filmController.findAll().size(), "Коллекция пуста");
    }

    @Test
    void create(){
        Film film = new Film();
        film.setName("nisi eiusmod");
        film.setDescription("adipisicing");
        film.setReleaseDate(LocalDate.of(1967, 03, 25));
        film.setDuration(100);
        film.setLikes(Set.of(1L, 2L, 3L));
        filmController.create(film);
        Film testFilm = new Film();
        testFilm.setName("nisi eiusmod");
        testFilm.setDescription("adipisicing");
        testFilm.setReleaseDate(LocalDate.of(1967, 03, 25));
        testFilm.setDuration(100);
        testFilm.setLikes(Set.of(1L, 2L, 3L));
        testFilm.setId(film.getId());
        assertEquals(testFilm, inMemoryFilmStorage.getFilm(film.getId()), "Фильм не добавлен");
    }

    @Test
    void saveFilm(){
        Film film = new Film();
        film.setName("nisi eiusmod");
        film.setDescription("adipisicing");
        film.setReleaseDate(LocalDate.of(1967, 03, 25));
        film.setDuration(100);
        film.setLikes(Set.of(1L, 2L, 3L));
        filmController.create(film);
        Film film2 = new Film();
        film2.setName("terminator");
        film2.setDescription("action");
        film2.setReleaseDate(LocalDate.of(1967, 03, 25));
        film2.setDuration(120);
        film2.setLikes(Set.of(7L, 4L, 8L));
        film2.setId(film.getId());
        filmController.saveFilm(film2);
        Film testFilm = new Film();
        testFilm.setName("terminator");
        testFilm.setDescription("action");
        testFilm.setReleaseDate(LocalDate.of(1967, 03, 25));
        testFilm.setDuration(120);
        testFilm.setLikes(Set.of(7L, 4L, 8L));
        testFilm.setId(film.getId());
        assertEquals(testFilm, inMemoryFilmStorage.getFilm(film2.getId()), "Фильм не добавлен");
    }
}
