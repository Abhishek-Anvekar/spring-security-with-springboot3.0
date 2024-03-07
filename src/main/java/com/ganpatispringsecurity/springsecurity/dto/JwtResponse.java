package com.ganpatispringsecurity.springsecurity.dto;

import lombok.*;

@Data
@Builder
public class JwtResponse {
    private String jwtToken;
    private String refreshToken;
    private UserDto userDto;
}
