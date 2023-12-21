package com.example.UserLogin.service;

import com.example.UserLogin.entity.User;
import com.example.UserLogin.exceptions.UserException;
import com.example.UserLogin.payload.UserDto;

public interface UserService {

    public User findUserByEmail(String email) throws UserException;

    String register(UserDto userDto) throws UserException;


}
