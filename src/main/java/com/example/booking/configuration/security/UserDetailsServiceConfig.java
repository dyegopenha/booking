package com.example.booking.configuration.security;

import com.example.booking.entity.UserEntity;
import com.example.booking.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserDetailsServiceConfig {

   private final UserRepository userRepository;

   public UserDetailsServiceConfig(UserRepository userRepository) {
      this.userRepository = userRepository;
   }

   @Bean
   public UserDetailsService userDetailsService() {
      return username -> {
         UserEntity user = userRepository.findByUsername(username)
                                         .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

         return org.springframework.security.core.userdetails.User.builder()
                                                                  .username(user.getUsername())
                                                                  .password(user.getPassword())
                                                                  .roles(user.getType().toString())
                                                                  .build();
      };
   }

   @Bean
   public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }
}
