package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;

@Service
public class UserIdGenerator {
    private long id = 0;

    public long generate() {
        id = id + 1;
        return id;
    }
}
