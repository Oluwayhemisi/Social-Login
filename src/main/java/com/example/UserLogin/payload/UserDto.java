package com.example.UserLogin.payload;

import com.example.UserLogin.entity.Role;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private Set<Role> roles;

}
