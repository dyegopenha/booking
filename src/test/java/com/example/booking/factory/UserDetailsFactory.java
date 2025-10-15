package com.example.booking.factory;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsFactory {

   public static UserDetails buildUserDetails(){
      return User.withUsername("springuser")
                 .password("springpassword")
                 .roles("USER")
                 .build();
   }
}
