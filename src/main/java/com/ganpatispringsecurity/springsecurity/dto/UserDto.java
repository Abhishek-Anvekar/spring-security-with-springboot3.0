package com.ganpatispringsecurity.springsecurity.dto;

import com.ganpatispringsecurity.springsecurity.entity.Role;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class UserDto {

    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Set<Role> roles = new HashSet<>();

}
