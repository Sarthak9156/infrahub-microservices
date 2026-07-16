package com.infrahub.authservice.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infrahub.authservice.entity.RefreshToken;
import com.infrahub.authservice.entity.User;
import com.infrahub.authservice.repository.RefreshTokenRepository;
import com.infrahub.authservice.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;
    
    public RefreshToken createRefreshToken(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        RefreshToken refreshToken = refreshTokenRepository
                .findByUser(user)
                .orElse(new RefreshToken());

        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusSeconds(60 * 60 * 24 * 7));

        return refreshTokenRepository.save(refreshToken);
    }
    public RefreshToken verifyExpiration(RefreshToken token) {

        if (token.getExpiryDate().isBefore(Instant.now())) {

            refreshTokenRepository.delete(token);

            throw new RuntimeException("Refresh Token Expired. Please Login Again");
        }

        return token;
    }

    
    public Optional<RefreshToken> findByToken(String token) {

        return refreshTokenRepository.findByToken(token);

    }
    @Transactional
    public void deleteByUserEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        refreshTokenRepository.deleteByUser(user);
    }
}
