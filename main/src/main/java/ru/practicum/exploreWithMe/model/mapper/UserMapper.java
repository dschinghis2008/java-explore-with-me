package ru.practicum.exploreWithMe.model.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.exploreWithMe.model.User;
import ru.practicum.exploreWithMe.model.dto.UserDto;

@Component
public class UserMapper {
    public UserDto toDto(User user) {
        if (user == null) {
            return null;
        }
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());
        return userDto;
    }

    public User toUser(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        User user = new User();
        if (userDto.getId() != null) {
            user.setId(userDto.getId());
        }

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return user;
    }
}
