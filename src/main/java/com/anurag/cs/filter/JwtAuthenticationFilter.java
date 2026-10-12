package com.anurag.cs.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.anurag.cs.entity.Student;
import com.anurag.cs.repository.StudentRepository;
import com.anurag.cs.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

      @Autowired
      private JwtService Jservice;

      @Autowired
      private StudentRepository repo;

      @Override
      protected void doFilterInternal(HttpServletRequest request,
                  HttpServletResponse response,
                  FilterChain filterChain) throws ServletException, IOException {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                  String token = authHeader.substring(7);
                  if (Jservice.validateToken(token)) {
                        String username = Jservice.extractUsername(token);// extracting email
                        Student std = repo.findByEmail(username);// verifying user details in database
                        if (std != null) {
                              UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                          std,
                                          null,
                                          List.of(
                                                      new SimpleGrantedAuthority("ROLE_" + std.getRole().name())));
                              SecurityContextHolder.getContext().setAuthentication(authentication);
                        }
                  }
            }
            filterChain.doFilter(request, response);
      }

}
