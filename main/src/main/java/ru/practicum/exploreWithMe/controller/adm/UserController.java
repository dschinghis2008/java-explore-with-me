package ru.practicum.exploreWithMe.controller.adm;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.model.User;
import ru.practicum.exploreWithMe.model.dto.UserDto;
import ru.practicum.exploreWithMe.model.mapper.UserMapper;
import ru.practicum.exploreWithMe.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/admin/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public UserDto add(@RequestBody @Valid UserDto userDto) {
        return userMapper.toDto(userService.add(userMapper.toUser(userDto)));
    }

    @GetMapping
    public List<UserDto> getUsers(@RequestParam(required = false) Long[] ids,
                                  @RequestParam(defaultValue = "10") @Positive int size,
                                  @RequestParam(defaultValue = "0") @PositiveOrZero int from) {
        List<UserDto> list = new ArrayList<>();
        for (User user : userService.getAllUsers(from, size, ids)) {
            list.add(userMapper.toDto(user));
        }
        return list;
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) {
        userService.delete(userId);
    }
}
