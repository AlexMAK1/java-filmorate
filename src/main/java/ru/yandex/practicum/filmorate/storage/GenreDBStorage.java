package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
        final List<Genre> genres = jdbcTemplate.query(sqlQuery, this::makeGenre, id);
        if (genres.size() != 1) {
            log.error("Ошибка, таблица GENRES пустая.");
            throw new NotFoundException("Ошибка, таблица GENRES пустая.");
        }
        return genres.get(0);
    }

    @Override
    public Set<Genre> getFilmGenres(long id) {
        if (id < 0) {
            log.error("Ошибка, валидация не пройдена. Id не может быть отрицательным: {}", id);
            throw new NotFoundException("Ошибка, валидация не пройдена. Id не может быть отрицательным.");
        }
        final String sqlQuery = "SELECT * FROM GENRES WHERE GENRE_ID IN " +
                "(SELECT FILM_GENRES.GENRE_ID FROM FILM_GENRES WHERE FILM_ID = ?) ORDER BY GENRE_ID";
        return new LinkedHashSet<>(jdbcTemplate.query(sqlQuery,this::makeGenre, id));
    }

    @Override
    public List<Genre> getGenres() {
        final String sqlQuery = "select * from GENRES";
        return jdbcTemplate.query(sqlQuery, this::makeGenre);
    }

    private Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getInt("GENRE_ID"),
                rs.getString("GENRE_NAME"));
    }
}
