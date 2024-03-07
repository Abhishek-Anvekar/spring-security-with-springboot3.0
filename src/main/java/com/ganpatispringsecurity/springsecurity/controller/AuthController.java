package com.ganpatispringsecurity.springsecurity.controller;

import com.ganpatispringsecurity.springsecurity.dto.JwtRequest;
import com.ganpatispringsecurity.springsecurity.dto.JwtResponse;
import com.ganpatispringsecurity.springsecurity.dto.RefreshTokenRequest;
import com.ganpatispringsecurity.springsecurity.dto.UserDto;
import com.ganpatispringsecurity.springsecurity.entity.RefreshToken;
import com.ganpatispringsecurity.springsecurity.entity.User;
import com.ganpatispringsecurity.springsecurity.exception.BadApiRequestException;
import com.ganpatispringsecurity.springsecurity.security.JwtHelper;
import com.ganpatispringsecurity.springsecurity.service.serviceImpl.RefreshTokenService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private RefreshTokenService refreshTokenService;

    @GetMapping("/welcome")
    ResponseEntity<String> getWelcomeMessage(){
        return new ResponseEntity<>("welcome.. welcome.. welcome", HttpStatus.OK);
    }

//    @GetMapping("/name")
//    ResponseEntity<UserDetails> getUserName(Principal principal){
//        String name = principal.getName();
//        return new ResponseEntity<>(userDetailsService.loadUserByUsername(name),HttpStatus.OK);
//    }

    @GetMapping("/name")
    ResponseEntity<UserDto> getUserName(Principal principal){
        String name = principal.getName();
        return new ResponseEntity<>(modelMapper.map(userDetailsService.loadUserByUsername(name),UserDto.class),HttpStatus.OK);
    }

    @PostMapping("/login")
    ResponseEntity<JwtResponse> login(@RequestBody JwtRequest jwtRequest){
        this.doAuthenticate(jwtRequest.getEmail(),jwtRequest.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequest.getEmail());
        String token = this.jwtHelper.generateToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);

        JwtResponse jwtResponse = JwtResponse.builder()
                .jwtToken(token)
                .refreshToken(refreshToken.getRefreshToken())
                .userDto(userDto)
                .build();

        return new ResponseEntity<>(jwtResponse,HttpStatus.OK);
    }

    private void doAuthenticate(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email,password);
        try {
            authenticationManager.authenticate(authenticationToken);
        }catch (BadCredentialsException e){
            throw new BadApiRequestException(" Invalid Username or Password  !!");
        }
    }

    @PostMapping("/refresh")
    JwtResponse refreshTwtToken(@RequestBody RefreshTokenRequest refreshTokenRequest){
        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(refreshTokenRequest.getRefreshToken());
        User user = refreshToken.getUser();
        String token = jwtHelper.generateToken(user);

        return JwtResponse.builder()
                .refreshToken(refreshToken.getRefreshToken())
                .jwtToken(token)
                .userDto(modelMapper.map(user,UserDto.class))
                .build();
    }
}
