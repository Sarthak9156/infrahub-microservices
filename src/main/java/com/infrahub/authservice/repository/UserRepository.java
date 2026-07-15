package com.infrahub.authservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.infrahub.authservice.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByEmail(String email);

}
