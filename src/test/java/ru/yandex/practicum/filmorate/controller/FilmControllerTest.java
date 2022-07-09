package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmIdGenerator;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.FilmValidationService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FilmControllerTest {

    FilmIdGenerator filmIdGenerator = new FilmIdGenerator();
    FilmValidationService filmValidationService = new FilmValidationService();
    InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage(filmIdGenerator, filmValidationService);
    FilmService filmService = new FilmService(inMemoryFilmStorage);
    FilmController filmController = new FilmController(filmService);

    @Test
    void findAll(){
        Film film = new Film();
        film.setName("nisi eiusmod");
        film.setDescription("adipisicing");
        film.setReleaseDate(LocalDate.of(1967, 03, 25));
        film.setDuration(100);
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
        filmController.create(film);
        Film testFilm = new Film();
        testFilm.setName("nisi eiusmod");
        testFilm.setDescription("adipisicing");
        testFilm.setReleaseDate(LocalDate.of(1967, 03, 25));
        testFilm.setDuration(100);
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
        filmController.create(film);
        Film film2 = new Film();
        film2.setName("terminator");
        film2.setDescription("action");
        film2.setReleaseDate(LocalDate.of(1967, 03, 25));
        film2.setDuration(120);
        film2.setId(film.getId());
        filmController.saveFilm(film2);
        Film testFilm = new Film();
        testFilm.setName("terminator");
        testFilm.setDescription("action");
        testFilm.setReleaseDate(LocalDate.of(1967, 03, 25));
        testFilm.setDuration(120);
        testFilm.setId(film.getId());
        assertEquals(testFilm, inMemoryFilmStorage.getFilm(film2.getId()), "Фильм не добавлен");
    }
}