package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.model.User;

import java.util.Collection;

public interface UserService {
    User add(User user);

    Collection<User> getUsers(Integer from, Integer size, Long[] arrId);

    User getById(Long id);

    void delete(Long id);
}
