package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserValidationService;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Primary
public class UserDbStorage implements UserStorage {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    private final UserValidationService userValidationService;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate, UserValidationService userValidationService) {
        this.jdbcTemplate = jdbcTemplate;
        this.userValidationService = userValidationService;
    }

    @Override
    public User create(User user) {
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        String sqlQuery = "insert into USERS (EMAIL, LOGIN, USER_NAME, BIRTHDAY ) values (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            final LocalDate birthday = user.getBirthday();
            if (birthday == null) {
                stmt.setNull(4, Types.DATE);
            } else {
                stmt.setDate(4, Date.valueOf(birthday));
            }
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().longValue());
        log.info("Сохраняем нового пользователя: {}", user);
        return user;
    }


    @Override
    public User getUser(long id) {
        if (id < 0) {
            log.error("Ошибка, валидация не пройдена. Id не может быть отрицательным: {}", id);
            throw new NotFoundException("Ошибка, валидация не пройдена. Id не может быть отрицательным.");
        }
        final String sqlQuery = "select * from USERS where USER_ID = ?";
        final List<User> users = jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser, id);
        if (users.size() != 1) {
            // TODO not found
        }
        return users.get(0);
    }

    private static User makeUser(ResultSet rs, int rowNum) throws SQLException {
        return new User(rs.getLong("USER_ID"),
                rs.getString("EMAIL"),
                rs.getString("LOGIN"),
                rs.getString("USER_NAME"),
                rs.getDate("BIRTHDAY").toLocalDate());
    }

    @Override
    public Map<Long, User> getUsers() {
        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }
}
