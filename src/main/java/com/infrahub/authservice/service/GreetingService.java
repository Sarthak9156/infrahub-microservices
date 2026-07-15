package com.infrahub.authservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import org.springframework.stereotype.Service;

import com.infrahub.authservice.dto.LoginRequestDTO;
import com.infrahub.authservice.dto.LoginResponseDTO;
import com.infrahub.authservice.dto.UserRequestDTO;
import com.infrahub.authservice.dto.UserResponseDTO;
import com.infrahub.authservice.entity.User;
import com.infrahub.authservice.exception.EmailAlreadyExistsException;
import com.infrahub.authservice.exception.UserNotFoundException;
import com.infrahub.authservice.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class GreetingService {

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
	    existingUser.setPassword(request.getPassword());

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
	            .orElseThrow(() -> new UserNotFoundException("Invalid Email"));

	    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
	        throw new RuntimeException("Invalid Password");
	    }

	    return new LoginResponseDTO("Login Successful");
	}
}