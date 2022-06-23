package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.*;

@Slf4j
@Component
@RestController
@RequestMapping("/films")
public class FilmController {

    private final String countValue = "10";
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Текущее количество фильмов: {}", filmService.getFilms().size());
        return filmService.getFilms().values();
    }

    @GetMapping("{id}")
    public Film getFilm(@PathVariable("id") long id) {
        return filmService.getById(id);
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("Сохраняем новый фильм: {}", film);
        return filmService.create(film);
    }

    @PutMapping
    public Film saveFilm(@RequestBody Film film) {
        return filmService.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") long id, @PathVariable("userId") long userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") long id, @PathVariable("userId") long userId) {
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> findCountPopularFilms(
            @RequestParam(defaultValue = countValue) Long count
    ) {
        if (count <= 0) {
            throw new IncorrectParameterException("count");
        }
        return filmService.getCountPopularFilms(count);
    }
}
