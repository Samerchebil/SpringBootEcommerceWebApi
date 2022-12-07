package com.rene.ecommerce.config;

import java.util.Arrays;

import com.rene.ecommerce.enume.ERole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.rene.ecommerce.security.JWTUtil;
import com.rene.ecommerce.security.filters.JWTAuthenticationFilter;
import com.rene.ecommerce.security.filters.JWTAuthorizationFilter;
import com.rene.ecommerce.services.details.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsServiceImpl userDetails;
	
	@Autowired
	private JWTUtil jwtUtil;

	
	private static final String[] PUBLIC_MATCHER = {
			"/swagger-ui/****","/v3/api-docs/**" ,"/create/**", "/forgot","/create/client","/create/seller","/create/client","/create/admin"
	};

	// Configure application: add auth filters, public endpoints 
	// and setting state (in this case, is STATELESS)
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.authorizeRequests().antMatchers(PUBLIC_MATCHER).permitAll();
		http.authorizeRequests().antMatchers(HttpMethod.PUT,"/update/seller").hasAnyAuthority("ROLE_SELLER");
		http.authorizeRequests().antMatchers(HttpMethod.PUT,"/update/client").hasAnyAuthority("ROLE_CLIENT");
		http.authorizeRequests().antMatchers(HttpMethod.PUT,"/update/client/{id}").hasAnyAuthority("ROLE_ADMIN");
		http.authorizeRequests().antMatchers(HttpMethod.PUT,"/update/seller/{id}").hasAnyAuthority("ROLE_ADMIN");
		http.authorizeRequests().antMatchers(HttpMethod.DELETE,"/delete/client/{id}").hasAnyAuthority("ROLE_ADMIN");
		http.authorizeRequests().antMatchers(HttpMethod.DELETE,"/delete/seller/{id}").hasAnyAuthority("ROLE_ADMIN");
		http.authorizeRequests().antMatchers(HttpMethod.PUT,"/update/admin").hasAnyAuthority("ROLE_ADMIN").anyRequest().authenticated();
		http.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtil));
		http.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtUtil, userDetails));
	}

	@Override
	public void configure(WebSecurity web) throws Exception
	{
		web.ignoring().antMatchers("/v2/api-docs",
				"/configuration/ui",
				"/swagger-resources/**",
				"/configuration/security",
				"/swagger-ui.html",
				"/webjars/**");
	}
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration().applyPermitDefaultValues();
        config.setAllowedMethods(Arrays.asList("*"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowedOriginPatterns(Arrays.asList("*"));
        config.setAllowedOrigins(Arrays.asList("*"));
        source.registerCorsConfiguration("/**", config);
        return source;
    }
	@Override
	    public void configure(AuthenticationManagerBuilder auth) throws Exception
	  {
		 auth.userDetailsService(userDetails).passwordEncoder(bCryptPasswordEncoder());
	    }

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
