package com.example.booking.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;

import com.example.booking.configuration.security.JwtUtil;
import com.example.booking.dto.response.AuthResponse;
import com.example.booking.factory.UserDetailsFactory;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class AuthServiceTest {

   @Mock
   private AuthenticationManager authenticationManager;

   @Mock
   private UserDetailsService userDetailsService;

   @Mock
   private JwtUtil jwtUtil;

   @InjectMocks
   private AuthServiceImpl authService;

   private final String expectedUsername = "springuser";
   private final String expectedPassword = "springpassword";
   private final String expectedValidToken = "valid-token";

   @Test
   public void whenLoginThenReturnValidToken(){
      UserDetails expectedUserDetails = UserDetailsFactory.buildUserDetails();

      when(authenticationManager.authenticate(any(Authentication.class)))
            .thenReturn(new UsernamePasswordAuthenticationToken(expectedUserDetails, null, expectedUserDetails.getAuthorities()));
      when(userDetailsService.loadUserByUsername(expectedUsername)).thenReturn(expectedUserDetails);
      when(jwtUtil.generateToken(expectedUsername)).thenReturn(expectedValidToken);

      AuthResponse result = authService.login(expectedUsername, expectedPassword);

      assertEquals(expectedValidToken, result.getToken());
   }

   @Test
   public void whenLoginThenThrowBadCredentialsException(){
      when(authenticationManager.authenticate(any(Authentication.class))).thenThrow(BadCredentialsException.class);

      assertThrows(BadCredentialsException.class, () -> {
         authService.login(expectedUsername, expectedPassword);
      });
   }
}
