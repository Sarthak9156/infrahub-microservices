package com.infrahub.authservice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.infrahub.authservice.dto.LoginRequestDTO;
import com.infrahub.authservice.dto.LoginResponseDTO;
import com.infrahub.authservice.dto.RefreshTokenRequestDTO;
import com.infrahub.authservice.dto.RefreshTokenResponseDTO;
import com.infrahub.authservice.dto.UserRequestDTO;
import com.infrahub.authservice.dto.UserResponseDTO;
import com.infrahub.authservice.entity.RefreshToken;
import com.infrahub.authservice.entity.Role;
import com.infrahub.authservice.entity.User;
import com.infrahub.authservice.exception.EmailAlreadyExistsException;
import com.infrahub.authservice.exception.InvalidCredentialsException;
import com.infrahub.authservice.exception.RefreshTokenNotFoundException;
import com.infrahub.authservice.exception.UserNotFoundException;
import com.infrahub.authservice.repository.UserRepository;
@Service
public class AuthService {
	@Autowired
	private RefreshTokenService refreshTokenService;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public List<UserResponseDTO> getAllUsers() {

	    List<User> users = userRepository.findAll();

	    List<UserResponseDTO> response = new ArrayList<>();

	    for (User user : users) {

	        response.add(new UserResponseDTO(
	                user.getId(),
	                user.getName(),
	                user.getEmail()));
	    }

	    return response;
	}

	public UserResponseDTO createUser(UserRequestDTO request) {

	    if (userRepository.findByEmail(request.getEmail()).isPresent()) {
	        throw new EmailAlreadyExistsException("Email already exists");
	    }

	    User user = new User();

	    user.setName(request.getName());
	    user.setEmail(request.getEmail());
	    user.setPassword(passwordEncoder.encode(request.getPassword()));
	    user.setRole(Role.USER);

	    userRepository.save(user);

	    return new UserResponseDTO(
	            user.getId(),
	            user.getName(),
	            user.getEmail());
	}

	public UserResponseDTO updateUser(int id, UserRequestDTO request) {

	    User existingUser = userRepository.findById(id)
	            .orElseThrow(() ->
	                    new UserNotFoundException("User Not Found"));

	    existingUser.setName(request.getName());
	    existingUser.setEmail(request.getEmail());

	    if (request.getPassword() != null && !request.getPassword().isBlank()) {
	        existingUser.setPassword(
	            passwordEncoder.encode(request.getPassword())
	        );
	    }

	    User updatedUser = userRepository.save(existingUser);

	    return new UserResponseDTO(
	            updatedUser.getId(),
	            updatedUser.getName(),
	            updatedUser.getEmail());
	}

	public String deleteUser(int id) {

		User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User Not Found"));

		userRepository.delete(user);

		return "User Deleted Successfully";
	}

	public UserResponseDTO getUserById(int id) {

		User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User Not Found"));

		return new UserResponseDTO(user.getId(), user.getName(), user.getEmail());
	}
	
	public LoginResponseDTO loginUser(LoginRequestDTO request) {

	    User user = userRepository.findByEmail(request.getEmail())
	            .orElseThrow(() ->
	                    new InvalidCredentialsException("Invalid Email or Password"));

	    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
	        throw new InvalidCredentialsException("Invalid Email or Password");
	    }

	    // Generate Access Token
	    String accessToken = jwtService.generateToken(user.getEmail());

	    // Generate Refresh Token
	    RefreshToken refreshToken =
	            refreshTokenService.createRefreshToken(user.getEmail());

	    return new LoginResponseDTO(
	            "Login Successful",
	            accessToken,
	            refreshToken.getToken()
	    );
	}

	public RefreshTokenResponseDTO refreshAccessToken(RefreshTokenRequestDTO request) {

	    RefreshToken refreshToken = refreshTokenService
	            .findByToken(request.getRefreshToken())
	            .orElseThrow(() ->
	                    new RefreshTokenNotFoundException("Refresh Token Not Found"));

	    refreshTokenService.verifyExpiration(refreshToken);

	    String accessToken =
	            jwtService.generateToken(refreshToken.getUser().getEmail());

	    return new RefreshTokenResponseDTO(accessToken);
	}

	public String logout(String email) {

	    refreshTokenService.deleteByUserEmail(email);

	    return "Logout Successful";
	}
	}
