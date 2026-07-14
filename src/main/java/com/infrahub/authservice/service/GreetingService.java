package com.infrahub.authservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;
import org.springframework.stereotype.Service;

import com.infrahub.authservice.dto.UserRequestDTO;
import com.infrahub.authservice.dto.UserResponseDTO;
import com.infrahub.authservice.entity.User;
import com.infrahub.authservice.exception.UserNotFoundException;
import com.infrahub.authservice.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class GreetingService {

	@Autowired
	private UserRepository userRepository;

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
		User user = new User();

		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPassword(request.getPassword());

		userRepository.save(user);

		return new UserResponseDTO(user.getId(), user.getName(), user.getEmail());
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
}