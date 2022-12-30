package ru.practicum.exploreWithMe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.exploreWithMe.exception.ConflictException;
import ru.practicum.exploreWithMe.exception.NotFoundException;
import ru.practicum.exploreWithMe.model.User;
import ru.practicum.exploreWithMe.repository.UserRepository;

import java.util.Arrays;
import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User add(User user) {
        if (userRepository.getCountByName(user.getName()) > 0) {
            throw new ConflictException(HttpStatus.CONFLICT);
        }
        log.info("--==>>USRSRV add user /{}/", user.getId());
        return userRepository.save(user);
    }

    @Override
    public Collection<User> getUsers(Integer from, Integer size, Integer[] arrId) {
        if (arrId.length > 0) {
            log.info("--==>>USRSRV query /{}/ users by id", arrId.length);
            return userRepository.findAllById(Arrays.asList(arrId));
        } else {
            Pageable pageable = PageRequest.of(from, size);
            log.info("--==>>USRSRV query users from=/{}/, size=/{}/", from, size);
            return userRepository.findAll(pageable).getContent();
        }
    }

    @Override
    public User getById(Integer id) {
        log.info("--==>>USRSRV query user with id=/{}/", id);
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
    }

    @Override
    public void delete(Integer id) {
        if (userRepository.findById(id).isPresent()) {
            log.info("--==>>USRSRV deleted user /{}/", id);
            userRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}