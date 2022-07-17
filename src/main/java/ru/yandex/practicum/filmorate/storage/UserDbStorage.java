package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserValidationService;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Repository
public class UserDbStorage implements UserStorage {

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
        userValidationService.validation(user);
        String sqlQuery = "insert into USERS (EMAIL, LOGIN, USER_NAME, BIRTHDAY ) values (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
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
        final List<User> users = jdbcTemplate.query(sqlQuery, this::makeUser, id);
        if (users.size() != 1) {
            // TODO not found
        }
        return users.get(0);
    }

    @Override
    public List<User> getUsers() {
        final String sqlQuery = "select * from USERS";
        return jdbcTemplate.query(sqlQuery, this::makeUser);
    }

    @Override
    public User update(User user) {
        if (user.getId() < 0) {
            log.error("Ошибка, валидация не пройдена. Id не может быть отрицательным: {}", user.getId());
            throw new NotFoundException("Ошибка, валидация не пройдена. Id не может быть отрицательным.");
        }
        String sqlQuery = "update USERS set " + "EMAIL = ?, LOGIN = ?, USER_NAME = ?, BIRTHDAY = ? " + "where USER_ID = ?";
        jdbcTemplate.update(sqlQuery
                , user.getEmail()
                , user.getLogin()
                , user.getName()
                , user.getBirthday()
                , user.getId());
        log.info("Добовляем пользователя: {}", user);
        return user;
    }

    @Override
    public void addFriend(long id, long friendId) {
        if (id < 0) {
            log.error("Ошибка, валидация не пройдена. Id не может быть отрицательным: {}", id);
            throw new NotFoundException("Ошибка, валидация не пройдена. Id не может быть отрицательным.");
        }
        if (friendId < 0) {
            log.error("Ошибка, валидация не пройдена. Id не может быть отрицательным: {}", id);
            throw new NotFoundException("Ошибка, валидация не пройдена. Id не может быть отрицательным.");
        }

        String sqlQuery = "INSERT INTO FRIENDS (USER_ID, FRIEND_ID) values (?, ?)" ;
        jdbcTemplate.update(sqlQuery,id, friendId);
    }

    @Override
    public void deleteFriend(long id, long friendId) {
        String sqlQuery = "DELETE FROM  FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sqlQuery, id, friendId);
    }

    public List<User> getCommonFriends(long userId, long otherId) {
        final String sqlQuery = "SELECT * FROM USERS WHERE USER_ID IN (select u.FRIEND_ID from FRIENDS u, FRIENDS o " +
                "where u.FRIEND_ID = o.FRIEND_ID " +
                "and u.USER_ID = ? " +
                "and o.USER_ID = ?)";
        return jdbcTemplate.query(sqlQuery, this::makeUser, userId, otherId);
    }

    @Override
    public List<User> getUserFriends(long userId) {
        final String sqlQuery = "SELECT * FROM USERS WHERE USER_ID IN (SELECT FRIEND_ID FROM FRIENDS WHERE USER_ID = ?)";
        return jdbcTemplate.query(sqlQuery, this::makeUser, userId);
    }

    private User makeUser(ResultSet rs, int rowNum) throws SQLException {
        return new User(rs.getLong("USER_ID"),
                rs.getString("EMAIL"),
                rs.getString("LOGIN"),
                rs.getString("USER_NAME"),
                rs.getDate("BIRTHDAY").toLocalDate());
    }
}
