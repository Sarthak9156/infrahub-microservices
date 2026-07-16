package com.infrahub.authservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.infrahub.authservice.dto.LoginRequestDTO;
import com.infrahub.authservice.dto.LoginResponseDTO;
import com.infrahub.authservice.dto.RefreshTokenRequestDTO;
import com.infrahub.authservice.dto.RefreshTokenResponseDTO;
import com.infrahub.authservice.dto.UserRequestDTO;
import com.infrahub.authservice.dto.UserResponseDTO;
import com.infrahub.authservice.service.AuthService;
import com.infrahub.authservice.service.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
public class AuthController {

	@Autowired
	private AuthService greetingService;

	@Autowired
	private JwtService jwtService;

	@GetMapping("/Allusers")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<List<UserResponseDTO>> getAllUsers() {

		List<UserResponseDTO> users = greetingService.getAllUsers();

		return ResponseEntity.ok(users);
	}

	@PostMapping("/users")
	public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(greetingService.createUser(request));

	}

	@GetMapping("/users/{id}")
	public ResponseEntity<UserResponseDTO> getUserById(@PathVariable int id) {

		return ResponseEntity.ok(greetingService.getUserById(id));
	}

	@PutMapping("/users/{id}")
	public ResponseEntity<UserResponseDTO> updateUser(@PathVariable int id,
			@Valid @RequestBody UserRequestDTO request) {

		UserResponseDTO updatedUser = greetingService.updateUser(id, request);

		return ResponseEntity.ok(updatedUser);
	}

	@DeleteMapping("/users/{id}")
	public String deleteUser(@PathVariable int id) {

		return greetingService.deleteUser(id);
	}
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> login(
	        @Valid @RequestBody LoginRequestDTO request) {

	    LoginResponseDTO response = greetingService.loginUser(request);

	    return ResponseEntity.ok(response);
	}
	
	@PostMapping("/refresh")
	public RefreshTokenResponseDTO refreshToken(
	        @RequestBody RefreshTokenRequestDTO request) {

	    return greetingService.refreshAccessToken(request);
	}
	
	@PostMapping("/logout")
	public ResponseEntity<String> logout(HttpServletRequest request) {
		
		

	    String authHeader = request.getHeader("Authorization");

	    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	        return ResponseEntity.badRequest().body("Token Missing");
	    }

	    String token = authHeader.substring(7);

	    String email = jwtService.extractUsername(token);

	    String message = greetingService.logout(email);

	    return ResponseEntity.ok(message);
	}

}