package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.model.User;

import java.util.Collection;

public interface UserService {
    User add(User user);

    Collection<User> getUsers(Integer from, Integer size, Integer[] arrId);

    User getById(Integer id);

    void delete(Integer id);
}
