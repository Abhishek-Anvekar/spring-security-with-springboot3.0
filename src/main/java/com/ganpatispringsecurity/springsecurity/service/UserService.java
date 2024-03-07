package com.ganpatispringsecurity.springsecurity.service;

import com.ganpatispringsecurity.springsecurity.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto addUser(UserDto userDto);
    List<UserDto> getAllUser();
    UserDto getUserById(long userId);
    UserDto updateUserById(long userId,UserDto userDto);
    String deleteUserById(long userId);
}
