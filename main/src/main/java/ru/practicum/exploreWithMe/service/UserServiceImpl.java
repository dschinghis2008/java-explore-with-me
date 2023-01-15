package ru.practicum.exploreWithMe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exploreWithMe.exception.NotFoundException;
import ru.practicum.exploreWithMe.model.User;
import ru.practicum.exploreWithMe.repository.UserRepository;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public User add(User user) {
        log.info("--==>>USRSRV add user /{}/", user.getId());
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers(Integer from, Integer size, Long[] arrId) {
        if (arrId == null || arrId.length == 0) {
            Pageable pageable = PageRequest.of(from, size);
            log.info("--==>>USRSRV query users from=/{}/, size=/{}/", from, size);
            return userRepository.findAll(pageable).getContent();
        } else {
            log.info("--==>>USRSRV query /{}/ users by id", arrId.length);
            return userRepository.findAllById(Arrays.asList(arrId));
        }
    }

    @Override
    public User getById(long id) {
        log.info("--==>>USRSRV query user with id=/{}/", id);
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
    }

    @Override
    @Transactional
    public void delete(long id) {
        if (userRepository.findById(id).isPresent()) {
            log.info("--==>>USRSRV deleted user /{}/", id);
            userRepository.deleteById(id);
        } else {
            throw new NotFoundException(HttpStatus.NOT_FOUND);
        }
    }
}