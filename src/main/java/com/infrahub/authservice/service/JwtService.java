package com.infrahub.authservice.service;

import java.util.Date;

import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private static final String SECRET_KEY = "mysecretkeymysecretkeymysecretkey123456";

	private SecretKey getSignInKey() {
	    return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
	}

	public String generateToken(String email) {

		return Jwts.builder().subject(email).issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)).signWith(getSignInKey()).compact();
	}

	private Claims extractAllClaims(String token) {

		return Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();
	}

	public String extractUsername(String token) {
		return extractAllClaims(token).getSubject();
	}

	public Date extractExpiration(String token) {
		return extractAllClaims(token).getExpiration();
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public boolean validateToken(String token, String email) {

		String username = extractUsername(token);

		return username.equals(email) && !isTokenExpired(token);
	}
}
