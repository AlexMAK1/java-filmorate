package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaStorage {

    Mpa getMpa(int id);

    List<Mpa> getMpas();
}
