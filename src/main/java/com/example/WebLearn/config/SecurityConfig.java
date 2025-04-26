package com.example.WebLearn.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Value("${jwt.signerKey}")
    private String signerKey;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(request -> request
                .requestMatchers(HttpMethod.POST, "/register", "/login").permitAll()
                .requestMatchers(HttpMethod.POST,//
                        "/create-classroom",
                        "/class/*/assignment",
                        "/class/*/lecture",
                        "/class/*/test"
                ).hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,//
                        "/update-classroom",
                        "/class/*/member",
                        "class/*/assignment/*",
                        "/class/*/lecture/*"
                ).hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE,//
                        "/class/*/d/*",
                        "/class/*/member/*",
                        "/class/*/assignment/*"
                ).hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST,//
                        "/class"
                ).hasRole("USER")
                .requestMatchers(HttpMethod.DELETE,//
                        "/class/*/assignment/*"
                ).hasRole("USER")
                .requestMatchers(HttpMethod.GET, "/**").permitAll()
                .requestMatchers("/ws/**", "/topic/**").permitAll()
                .anyRequest()
                .authenticated());
        httpSecurity.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer -> jwtConfigurer
                .decoder(jwtDecoder())
                .jwtAuthenticationConverter(jwtAuthenticationConverter()))
        );
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }

    @Bean
    JwtDecoder jwtDecoder() {
        SecretKeySpec secretKeySpec= new SecretKeySpec(signerKey.getBytes(), "HS512");
        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();

    };

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("scope");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfigurer = new CorsConfiguration();
        corsConfigurer.addAllowedHeader("*");
        corsConfigurer.addAllowedMethod("*");
        corsConfigurer.addAllowedOrigin("*");
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfigurer);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }
}
