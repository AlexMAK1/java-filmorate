package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Repository
public class MpaDbStorage implements MpaStorage{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> getMpas() {
        final  String sqlQuery = "select * from MPA";
        return jdbcTemplate.query(sqlQuery, MpaDbStorage :: makeMpa);
    }

    private static Mpa makeMpa(ResultSet rs, int rowNum) throws SQLException {
        return new Mpa(rs.getInt("MPA_ID"),
                rs.getString("MPA_NAME"));
    }

    @Override
    public Mpa getMpa(int id) {
        if (id < 0) {
            log.error("Ошибка, валидация не пройдена. Id не может быть отрицательным: {}", id);
            throw new NotFoundException("Ошибка, валидация не пройдена. Id не может быть отрицательным.");
        }
        final String sqlQuery = "select * from MPA where MPA_ID = ?";
        final List<Mpa> mpas = jdbcTemplate.query(sqlQuery, MpaDbStorage :: makeMpa, id);
        return mpas.get(0);
    }
}
