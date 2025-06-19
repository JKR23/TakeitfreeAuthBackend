package com.takeitfree.auth.config.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * JwtUtils :
 * Description: This class handles all operations related to JWT (JSON Web Token).
 * Purpose:
 *  - Generate JWT tokens.
 *  - Extract data (claims) from tokens (e.g., username).
 *  - Validate tokens.
 *  - Use secret key to sign and verify tokens.
 */
@Service
@RequiredArgsConstructor
public class JwtUtils {

    /// Converts the secret key string into a Key object that can be used with the JJWT library
    @Value("${spring.security.oauth2.resourceserver.jwt.secret-key}")
    private String SECRET_KEY;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    /**
     * Generates a JWT token using the user's details and user ID.
     *
     * @param userDetails the user's security details (such as username and authorities)
     * @param userId the unique ID of the user
     * @return a signed JWT token containing user information and claims
     */
    public String generateToken(UserDetails userDetails, Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        return createToken(claims, userDetails);
    }


    /// Internal method that builds the actual token with claims and signs it
    /**
     * @param claims represent...
     * @param userDetails  represent user in the spring security*/
    private String createToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(claims) // additional data to include
                .setSubject(userDetails.getUsername()) // username(represent an email) as the subject
                .claim("authorities", userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList()
                ) // optionally add roles or permissions
                .setIssuedAt(new Date(System.currentTimeMillis())) // when token was created
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(8))) // expiration date (8h)
                .signWith(SignatureAlgorithm.HS256, getSigningKey()) // sign with key and algorithm
                .compact(); // return the token as a string
    }

    /**
     * Extracts a specific claim from the JWT token using a claims resolver function.
     *
     * @param token the JWT token
     * @param claimsResolver a function that defines which claim to extract (e.g., expiration, subject)
     * @return the value of the extracted claim
     * @param <T> the type of the claim's value
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Parses the JWT token and returns all its claims (payload data).
     *
     * @param token the JWT token
     * @return the Claims object containing all the token's data
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(getSigningKey()).parseClaimsJws(token).getBody();
    }

    /**
     * Extracts the expiration date from the JWT token.
     *
     * @param token the JWT token
     * @return the expiration date of the token
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


}
