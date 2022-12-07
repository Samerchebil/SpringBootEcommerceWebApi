package com.rene.ecommerce.security.filters;

import java.io.IOException;
import java.util.*;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rene.ecommerce.security.AdminSS;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rene.ecommerce.domain.dto.AuthDTO;
import com.rene.ecommerce.security.ClientSS;
import com.rene.ecommerce.security.JWTUtil;
import com.rene.ecommerce.security.SellerSS;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;

	private JWTUtil jwtUtil;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
		setAuthenticationFailureHandler(new JWTAuthenticationFailureHandler());
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
	}

	// Try to authenticate
	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
			throws AuthenticationException {

		try {
			AuthDTO creds = new ObjectMapper().readValue(req.getInputStream(), AuthDTO.class);
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(creds.getEmail(),
					creds.getPassword(), new ArrayList<>());

			Authentication auth = authenticationManager.authenticate(authToken);
			return auth;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	// If authenticate sucess, search which user the Auth obj is, wheter is 
	// client or seller
	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
			Authentication auth) throws IOException, ServletException {

		String username = "";
		Collection<? extends GrantedAuthority> authorities ;

		try {
			username = ((ClientSS) auth.getPrincipal()).getUsername();
			authorities = ((ClientSS) auth.getPrincipal()).getAuthorities();

		} catch (ClassCastException e) {
			try{
				username = ((SellerSS) auth.getPrincipal()).getUsername();
				authorities = ((SellerSS) auth.getPrincipal()).getAuthorities();
			} catch (ClassCastException e2) {
				username = ((AdminSS) auth.getPrincipal()).getUsername();
				authorities = ((AdminSS) auth.getPrincipal()).getAuthorities();			}
		}

		String token = jwtUtil.generateToken(username,authorities);
		String refresh_token =jwtUtil.generateRefreshToken(username);
		res.addHeader("Authorization", "Bearer " + token);
		res.addHeader("refresh_token", "Bearer " + refresh_token);
		res.addHeader("access-control-expose-headers", "Authorization");
		res.addHeader("Access-Control-Expose-Headers", "Authorization");
		res.addHeader("Access-Control-Allow-Headers", "Authorization, X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept, X-Custom-header");
		Map<String, String> tokens =new HashMap<>();
		tokens.put("access_token", token);
		tokens.put("refresh_token", refresh_token);
		res.setContentType("application/json");
		new ObjectMapper().writeValue(res.getOutputStream(), tokens);
	}

	
	// If the authenticate fails, throw the json below 
	private class JWTAuthenticationFailureHandler implements AuthenticationFailureHandler {

		@Override
		public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
			logger.error("Authentication failed: " + exception.getMessage());
			response.setStatus(401);
			response.setContentType("application/json");
			response.getWriter().append(json());
		}

		private String json() {
			long date = new Date().getTime();
			return "{\"timestamp\": " + date + ", " + "\"status\": 401, " + "\"error\": \"Not authorized\", "
					+ "\"message\": \"Email or password incorrects\", " + "\"path\": \"/login\"}";
		}
	}
}
