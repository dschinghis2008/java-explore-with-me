package ru.practicum.exploreWithMe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.exploreWithMe.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("select count(u) from User u where u.name=:name")
    Integer getCountByName(String name);
}
