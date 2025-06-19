package com.takeitfree.auth.config.utils;

import com.takeitfree.auth.service.BlackListedTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JwtBlacklistFilter:
 * A filter that intercepts every HTTP request to check if the JWT token is blacklisted (revoked).
 * Purpose:
 * - Ensures that users cannot reuse tokens that have been explicitly invalidated (e.g., after logout).
 * - Responds with HTTP 401 Unauthorized if the token is blacklisted/Expired.
 */
@Component
@RequiredArgsConstructor
public class JwtBlackListFilter extends OncePerRequestFilter {

    private final BlackListedTokenService blackListedTokenService;
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private final JwtUtils jwtUtils;

    /**
     * Filters each request to verify if the provided JWT is blacklisted.
     *
     * @param request      the HTTP request
     * @param response     the HTTP response
     * @param filterChain  the remaining filters in the chain
     * @throws ServletException if the filter fails during processing
     * @throws IOException if an I/O error occurs during filtering
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Extract the Authorization header
        String authHeader = request.getHeader(AUTHORIZATION);

        // Proceed if the header contains a Bearer token
        if (authHeader != null && authHeader.startsWith(BEARER)) {
            String jwt = authHeader.substring(7); // Remove "Bearer " prefix

            // Check if the token is blacklisted
            if (blackListedTokenService.isBlackListedTokenFromLogOut(jwt)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token has been revoked");
                return;
            }
        }

        // Continue with the filter chain if token is not blacklisted
        filterChain.doFilter(request, response);
    }
}
