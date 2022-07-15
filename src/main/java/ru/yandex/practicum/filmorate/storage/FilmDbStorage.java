package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmValidationService;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Repository
public class FilmDbStorage implements FilmStorage {

    private final FilmValidationService filmValidationService;
    private final MpaDbStorage mpaDbStorage;
    private final GenreDBStorage genreDBStorage;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(FilmValidationService filmValidationService, MpaDbStorage mpaDbStorage, GenreDBStorage genreDBStorage, JdbcTemplate jdbcTemplate) {
        this.filmValidationService = filmValidationService;
        this.mpaDbStorage = mpaDbStorage;
        this.genreDBStorage = genreDBStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film getFilm(long id) {
        if (id < 0) {
            log.error("Ошибка, валидация не пройдена. Id не может быть отрицательным: {}", id);
            throw new NotFoundException("Ошибка, валидация не пройдена. Id не может быть отрицательным.");
        }
        final String sqlQuery = "select * from FILMS where FILM_ID = ?";
        final List<Film> films = jdbcTemplate.query(sqlQuery, FilmDbStorage::makeFilm, id);
        if (films.size() != 1) {
            // TODO not found
        }
        Film film = films.get(0);
        List<Genre> genres = genreDBStorage.getFilmGenres(film.getId());
        film.setGenres(genres);
        mpaDbStorage.getMpa(film.getMpa().getId());
        return film;
    }

    @Override
    public List<Film> getFilms() {
        String sqlQuery = "SELECT * FROM FILMS";
        final List<Film> films = jdbcTemplate.query(sqlQuery, FilmDbStorage::makeFilm);
        for (Film film : films) {
            genreDBStorage.getFilmGenres(film.getId());
            mpaDbStorage.getMpa(film.getMpa().getId());
        }
        return films;
    }

    private static Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        return new Film(rs.getLong("FILM_ID"),
                rs.getString("FILM_NAME"),
                rs.getDate("RELEASE_DATE").toLocalDate(),
                rs.getString("DESCRIPTION"),
                rs.getInt("DURATION"));
    }

    @Override
    public Film create(Film film) {
        filmValidationService.validation(film);
        String sqlQuery = "insert into FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID ) values (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            final LocalDate releaseDate = film.getReleaseDate();
            if (releaseDate == null) {
                stmt.setNull(3, Types.DATE);
            } else {
                stmt.setDate(3, Date.valueOf(releaseDate));
            }
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());
        log.info("Сохраняем новый фильм: {}", film);
        setFilmGenre(film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (film.getId() < 0) {
            log.error("Ошибка, валидация не пройдена. Id не может быть отрицательным: {}", film.getId());
            throw new NotFoundException("Ошибка, валидация не пройдена. Id не может быть отрицательным.");
        }
        String sqlQuery = "update FILMS set " + "FILM_NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, MPA_ID =?" + "where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );
        updateFilmGenre(film);
        log.info("Добовляем пользователя: {}", film);
        return film;
    }



    @Override
    public void setFilmGenre(Film film) {
        for (Genre genre : film.getGenres()) {
            final String sqlQuery = "insert into FILM_GENRES (FILM_ID, GENRE_ID) values ( ? ,? )";
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(sqlQuery);
                stmt.setLong(1, film.getId());
                stmt.setInt(2, genre.getId());
                return stmt;
            })
            ;
        }
    }

    @Override
    public void updateFilmGenre(Film film) {
        String sqlQuery = "DELETE FROM FILM_GENRES where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
        setFilmGenre(film);
    }
}
