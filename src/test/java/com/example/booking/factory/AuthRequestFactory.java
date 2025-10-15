package com.example.booking.factory;

import com.example.booking.dto.request.AuthRequest;

public class AuthRequestFactory {

   public static AuthRequest buildValidAuthRequest(){
      return AuthRequest
            .builder()
            .username("guest_user")
            .password("123456")
            .build();
   }

   public static AuthRequest buildValidPropertyOwnerAuthRequest(){
      return AuthRequest
            .builder()
            .username("property_owner")
            .password("admin")
            .build();
   }

   public static AuthRequest buildInvalidAuthRequest(){
      return AuthRequest
            .builder()
            .username("baah")
            .password("9999")
            .build();
   }
}
