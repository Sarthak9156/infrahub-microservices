package com.infrahub.authservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.infrahub.authservice.entity.RefreshToken;
import com.infrahub.authservice.entity.User;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

	Optional<RefreshToken> findByToken(String token);

	Optional<RefreshToken> findByUser(User user);

	void deleteByUser(User user);
}
