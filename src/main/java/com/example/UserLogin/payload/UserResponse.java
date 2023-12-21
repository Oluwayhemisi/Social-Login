package com.example.UserLogin.payload;

import com.example.UserLogin.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private LocalDate dateJoined;

    private Set<Role> roles;
}
