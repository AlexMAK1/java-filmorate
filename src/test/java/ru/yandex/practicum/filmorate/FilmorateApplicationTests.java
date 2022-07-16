package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.GenreDBStorage;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmDbStorage;
    private final GenreDBStorage genreDBStorage;

    private final MpaDbStorage mpaDbStorage;

    @Test
    public void testFindUserById() {
        User user = userStorage.getUser(1);
        assertNotNull(user);
        assertEquals(1, user.getId());
    }

    @Test
    public void testFindFilmById() {
        Film film = filmDbStorage.getFilm(1);
        assertNotNull(film);
        assertEquals(1, film.getId());
    }

    @Test
    public void testFindGenreDyId(){
        Genre genre = genreDBStorage.getGenre(1);
        assertNotNull(genre);
        assertEquals("Комедия", genre.getName());
    }

    @Test
    public void testFindMpabyId(){
        Mpa mpa = mpaDbStorage.getMpa(1);
        assertNotNull(mpa);
        assertEquals("G", mpa.getName());
    }
}
