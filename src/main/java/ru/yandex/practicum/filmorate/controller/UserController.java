package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.Set;

@Slf4j
@Component
@RestController
@RequestMapping("/users")
public class UserController {


    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> findAll() {
        log.info("Текущее количество пользователей: {}", userService.getInMemoryUserStorage().getUsers().size());
        return userService.getInMemoryUserStorage().getUsers().values();
    }

    @GetMapping("{id}")
    public User getUser(@PathVariable("id") long id) {
        return userService.getInMemoryUserStorage().getUser(id);
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return userService.getInMemoryUserStorage().create(user);
    }

    @PutMapping
    public User saveUser(@RequestBody User user) {
        return userService.getInMemoryUserStorage().update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") long id, @PathVariable("friendId") long friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") long id, @PathVariable("friendId") long friendId) {
         userService.deleteFriend(id, friendId);
    }

    @GetMapping("{id}/friends")
    public Set<Long> getFriends(@PathVariable("id") long id) {
        return userService.getFriends(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public Set<Long> getCommonFriends(@PathVariable("id") long id, @PathVariable("friendId") long otherId) {
        return userService.getCommonFriends(id, otherId);
    }
}
