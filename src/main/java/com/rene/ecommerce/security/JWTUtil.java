package com.rene.ecommerce.security;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtil {

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private Long expiration;
	@Value("${jwt.refresh_expiration}")
	private Long refreshExpiration;

	public String generateToken(String username,Collection<? extends GrantedAuthority> role) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("roles", role.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
		return Jwts.builder().setClaims(claims).setSubject(username).setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(SignatureAlgorithm.HS512, secret.getBytes()).compact();
	}

	public String generateRefreshToken(String username) {
		return Jwts.builder().setSubject(username).setExpiration(new Date(System.currentTimeMillis() + refreshExpiration))
				.signWith(SignatureAlgorithm.HS512, secret.getBytes()).compact();

	}

	public String getEmail(String token) {
		
     Claims claims = getClaims(token);
		
		if(claims != null) {
			return claims.getSubject();
		}
		return null;
	}

	public boolean isTokenValid(String token) {
		Claims claims = getClaims(token);
		
		if(claims != null) {
			String email = claims.getSubject();
			Date expirationDate = claims.getExpiration();
			Date now = new Date(System.currentTimeMillis());
			
			if(email != null && expirationDate != null && now.before(expirationDate)) {
				return true;
			}
			return false;
		}
		return false;
	}

	private Claims getClaims(String token) {
		try {
			return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();

		} catch (Exception e) {
			return null;
		}
	}
}
