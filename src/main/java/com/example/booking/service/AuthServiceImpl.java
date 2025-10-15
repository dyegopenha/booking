package com.example.booking.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.example.booking.configuration.security.JwtUtil;
import com.example.booking.dto.response.AuthResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

   private final AuthenticationManager authenticationManager;

   private final UserDetailsService userDetailsService;

   private final JwtUtil jwtUtil;

   @Override
   public AuthResponse login(String username, String password) {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
      final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
      final String jwt = jwtUtil.generateToken(userDetails.getUsername());
      return new AuthResponse(jwt);
   }
}
