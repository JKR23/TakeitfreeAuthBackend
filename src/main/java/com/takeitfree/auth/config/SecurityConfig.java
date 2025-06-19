package com.takeitfree.auth.config;

import com.takeitfree.auth.config.utils.JwtBlackListFilter;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * SecurityConfig :
 * Description: This class configures Spring Security for the application.
 * Purpose:
 *  - Set security rules (which endpoints require authentication or not).
 *  - Register the custom JWT filter.
 *  - Define how users are authenticated and passwords are encoded.
 */
@Configuration // Indicates that this class contains Spring beans and configuration
@EnableWebSecurity // Enables Spring Security support
@RequiredArgsConstructor
@EnableMethodSecurity //protect some gateway for just admin, take a look userController
public class SecurityConfig {

    // Injects a custom UserDetailsService (used for fetching user info from DB)
    private final UserDetailsService userDetailsService;

    private final JwtBlackListFilter jwtBlackListFilter;

    //load the value of the key from application.properties
    @Value("${spring.security.oauth2.resourceserver.jwt.secret-key}")
    private String secretKey;

    /**
     * Configures the HTTP security settings (authorization rules, filter chain, etc.)
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth/**"
                        ).permitAll()
                        //.requestMatchers("/role/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(new CustomJwtAuthenticationConverter())
                        )
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        //verify if the token that user try to connect with isBlackListed or not
        http.addFilterBefore(jwtBlackListFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000")); // Allows frontend on port 3000
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Allowed HTTP methods
        config.setAllowedHeaders(List.of("*")); // Allows any headers
        config.setAllowCredentials(true); // Allow cookies or Authorization headers

        // Apply this configuration to all routes
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    /**
     * Bean to configure JwtDecoder with secret key
     * This decoder is necessary to validate the JWT tokens
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return NimbusJwtDecoder.withSecretKey(new SecretKeySpec(keyBytes, "HmacSHA256")).build();
    }

    /**
     * Registers the authentication manager bean, which is responsible for processing login attempts.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Defines the authentication provider that uses the custom UserDetailsService
     * and checks passwords using the BCrypt algorithm.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(this.userDetailsService); // link to user repository
        authenticationProvider.setPasswordEncoder(passwordEncoder()); // password encryption

        return authenticationProvider;
    }

    /**
     * Defines the password encoder used to store and check passwords in a secure way.
     * BCrypt is strong and widely used.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
