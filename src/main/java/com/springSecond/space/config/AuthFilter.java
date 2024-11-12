package com.springSecond.space.config;
import jakarta.servlet.FilterChain;
import com.springSecond.space.utils.JwtUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService; // Ensure this is the same UserDetailsService used in your app

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        // Skip authentication for login and register endpoints
        if (request.getRequestURI().startsWith("/api/v2/auth/login")
                || request.getRequestURI().startsWith("/api/v2/auth/register")
                || request.getRequestURI().startsWith("/swagger-ui")
                || request.getRequestURI().startsWith("/v3/api-docs")
                || request.getRequestURI().startsWith("/swagger-resources")
                || request.getRequestURI().startsWith("/webjars")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = null;
        String userId = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            userId = jwtUtils.extractUserId(token); // Assuming extractUserId returns the username or ID

            if (jwtUtils.validateToken(token) && userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Load user details to set authentication
                UserDetails userDetails = userDetailsService.loadUserByUsername(userId);

                if (jwtUtils.validateToken(token)) {
                    // Create the authentication object
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Set authentication in context
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
