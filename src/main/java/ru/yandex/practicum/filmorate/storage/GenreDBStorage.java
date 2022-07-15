package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Repository
public class GenreDBStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public GenreDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getGenre(int id) {
        if (id < 0) {
            log.error("Ошибка, валидация не пройдена. Id не может быть отрицательным: {}", id);
            throw new NotFoundException("Ошибка, валидация не пройдена. Id не может быть отрицательным.");
        }
        final String sqlQuery = "select * from GENRES where GENRE_ID = ?";
        final List<Genre> genres = jdbcTemplate.query(sqlQuery, GenreDBStorage::makeGenre, id);
        if (genres.size() != 1) {
            // TODO not found
        }
        return genres.get(0);
    }

    @Override
    public List<Genre> getFilmGenres(long id) {
        List<Genre> genres;
        try {
            if (id < 0) {
                log.error("Ошибка, валидация не пройдена. Id не может быть отрицательным: {}", id);
                throw new NotFoundException("Ошибка, валидация не пройдена. Id не может быть отрицательным.");
            }
            final String sqlQuery = "SELECT * FROM GENRES WHERE GENRE_ID IN (SELECT FILM_GENRES.GENRE_ID FROM FILM_GENRES WHERE FILM_ID = ?)";
            genres = new LinkedList<>(jdbcTemplate.query(sqlQuery, GenreDBStorage::makeGenre, id));
        } catch (Throwable e) {
            throw new ValidationException(e.getMessage());
        }
        return genres;
    }

    private static Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getInt("GENRE_ID"),
                rs.getString("GENRE_NAME"));
    }

    @Override
    public List<Genre> getGenres() {
        final String sqlQuery = "select * from GENRES";
        return jdbcTemplate.query(sqlQuery, GenreDBStorage::makeGenre);
    }
}