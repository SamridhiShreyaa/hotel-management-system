package com.pesu.hotel.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers("/", "/auth/register", "/auth/login", "/css/**", "/js/**", "/images/**").permitAll()
						.requestMatchers("/admin/**").hasRole("ADMIN")
						.requestMatchers("/payment/**", "/reservations/**", "/auth/room-search").authenticated()
						.anyRequest().authenticated())
					.formLogin(form -> form
						.loginPage("/auth/login")
						.loginProcessingUrl("/auth/login")
						.usernameParameter("email")
						.passwordParameter("password")
						.successHandler((request, response, authentication) -> {
							if (hasRole(authentication, "ROLE_ADMIN")) {
								response.sendRedirect("/admin/dashboard");
								return;
							}
							if (hasRole(authentication, "ROLE_GUEST")) {
								response.sendRedirect("/auth/room-search");
								return;
							}
							response.sendRedirect("/auth/login?error=true");
						})
						.failureUrl("/auth/login?error=true")
						.permitAll())
					.logout(logout -> logout.logoutUrl("/auth/logout").logoutSuccessUrl("/auth/login?logout=true"));
		return http.build();
	}

	private boolean hasRole(Authentication authentication, String role) {
		return authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.anyMatch(role::equals);
	}
}
