package ru.practicum.exploreWithMe.controller.adm;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.model.dto.UserDto;
import ru.practicum.exploreWithMe.model.mapper.UserMapper;
import ru.practicum.exploreWithMe.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/admin/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<UserDto> add(@RequestBody @Valid UserDto userDto) {
        return new ResponseEntity<>(userMapper.toDto(userService.add(userMapper.toUser(userDto))), HttpStatus.CREATED);
    }

    @GetMapping
    public List<UserDto> getUsers(@RequestParam(required = false) Long[] ids,
                                  @RequestParam(defaultValue = "10") @Positive int size,
                                  @RequestParam(defaultValue = "0") @PositiveOrZero int from) {
        return userService.getAllUsers(from, size, ids).stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Integer> deleteUser(@PathVariable long userId) {
        userService.delete(userId);
        return new ResponseEntity<>(204, HttpStatus.NO_CONTENT);
    }
}
