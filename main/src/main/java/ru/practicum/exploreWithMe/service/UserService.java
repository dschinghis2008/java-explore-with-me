package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.model.User;

import java.util.List;

public interface UserService {
    User add(User user);

    List<User> getAllUsers(Integer from, Integer size, Long[] arrId);

    User getById(long id);

    void delete(long id);
}
