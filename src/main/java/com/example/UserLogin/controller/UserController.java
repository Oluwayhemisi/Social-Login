package com.example.UserLogin.controller;


import com.example.UserLogin.entity.User;
import com.example.UserLogin.exceptions.UserException;
import com.example.UserLogin.payload.*;
import com.example.UserLogin.service.UserService;
import com.example.UserLogin.usersecurity.jwt.TokenProvider;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    private final TokenProvider tokenProvider;




    @PostMapping("/register")
    public ResponseEntity<String> Register(@Valid @RequestBody UserDto userDto){
        String response = userService.register(userDto);
         return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<LoginResponseDto> loginUser(@Valid @RequestBody LoginDto loginDto) throws UserException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        User user = userService.findUserByEmail(loginDto.getEmail());
//        if (!user) {
//            throw new StyleMeException("Email is not verified", HttpStatus.UNAUTHORIZED);
//        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = tokenProvider.generateJWTToken(authentication);
        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setFirstName(user.getFirstName());
        loginResponseDto.setLastName(user.getLastName());
        loginResponseDto.setEmail(user.getEmail());
        loginResponseDto.setToken(token);
        return ResponseEntity.ok(loginResponseDto);
    }
}
