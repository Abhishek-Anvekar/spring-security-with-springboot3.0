package com.ganpatispringsecurity.springsecurity.dto;

import lombok.*;

@Data
@Builder
public class JwtRequest {

    private String email;
    private String password;

}
