package com.example.cloudnative.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtGrantedAuthoritiesConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.savedrequest.NoOpServerRequestCache;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	@Bean
	SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
		return http
				.authorizeExchange(exchange -> exchange
						.anyExchange().hasRole("employee"))
				
				.oauth2ResourceServer(oauth2 -> oauth2
						.jwt(Customizer.withDefaults()))

				.requestCache(requestCacheSpec ->
						requestCacheSpec.requestCache(NoOpServerRequestCache.getInstance()))
				
				.csrf(ServerHttpSecurity.CsrfSpec::disable)
				.build();
	}
	
	@Bean
	public ReactiveJwtAuthenticationConverter jwtAuthenticationConverter() {
	    var jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
	    jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
	    jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");

	    var jwtAuthenticationConverter = new ReactiveJwtAuthenticationConverter();
	    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new ReactiveJwtGrantedAuthoritiesConverterAdapter(jwtGrantedAuthoritiesConverter));

	    return jwtAuthenticationConverter;
	}
	
}