package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;

@Service
public class FilmIdGenerator {
    private long id = 0L;

    public long generate() {
        id = id + 1L;
        return id;
    }
}
