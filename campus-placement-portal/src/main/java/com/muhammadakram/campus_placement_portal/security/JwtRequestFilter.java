
package com.muhammadakram.campus_placement_portal.security;

import com.muhammadakram.campus_placement_portal.entity.User;
import com.muhammadakram.campus_placement_portal.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        return request.getMethod().equals("OPTIONS")
                || path.equals("/api/users/login")
                || path.equals("/api/users/register")
                || path.equals("/api/users/forgot-password")
                || path.equals("/api/users/reset-password");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null ||
                !authorizationHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String jwt = authorizationHeader.substring(7);

        try {
            String email = jwtUtil.extractEmail(jwt);

            if (email == null || !jwtUtil.isTokenValid(jwt, email)) {
                response.sendError(401, "Invalid or expired token");
                return;
            }

            if (SecurityContextHolder.getContext().getAuthentication() == null) {

                User user = userRepository.findByEmail(email);

                if (user == null) {
                    response.sendError(401, "User not found");
                    return;
                }

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                new ArrayList<>()
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder.getContext()
                        .setAuthentication(authToken);
            }

        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            response.sendError(401, "Invalid or expired token");
            return;
        }

        chain.doFilter(request, response);
    }
}
