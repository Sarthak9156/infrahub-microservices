package com.infrahub.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.infrahub.authservice.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}
