package com.ganpatispringsecurity.springsecurity.service.serviceImpl;

import com.ganpatispringsecurity.springsecurity.entity.RefreshToken;
import com.ganpatispringsecurity.springsecurity.entity.User;
import com.ganpatispringsecurity.springsecurity.repository.RefreshTokenRepository;
import com.ganpatispringsecurity.springsecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserRepository userRepository;

//    public long refreshTokenValidity= 5*60*60*1000;
    public long refreshTokenValidity= 2*60*1000; // for 2 min - to check refresh token expiring or not

    public RefreshToken createRefreshToken(String username){
        User user = userRepository.findByEmail(username).get();
        RefreshToken refreshToken1 = user.getRefreshToken();

        if (refreshToken1 == null){
            refreshToken1 = RefreshToken.builder()
                    .refreshToken(UUID.randomUUID().toString())
                    .expiry(Instant.now().plusMillis(refreshTokenValidity))
                    .user(userRepository.findByEmail(username).get())
                    .build();
        }else {
            refreshToken1.setExpiry(Instant.now().plusMillis(refreshTokenValidity));
        }

        user.setRefreshToken(refreshToken1);
        //save refreshToken to DB
        refreshTokenRepository.save(refreshToken1);
        return refreshToken1;
    }

    public RefreshToken verifyRefreshToken(String refreshToken){
        RefreshToken refreshTokenDb = refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow(() -> new RuntimeException("Given token does not exist !!"));

        // if refresh token expiry time is already passed then when we compare with current time then it return negative value
        if (refreshTokenDb.getExpiry().compareTo(Instant.now()) < 0){
            refreshTokenRepository.delete(refreshTokenDb);
            throw new RuntimeException("Refresh token is expired !!");
        }else {
            return refreshTokenDb;
        }
    }
}


