package com.example.booking.factory;

import com.example.booking.dto.response.UserResponse;
import com.example.booking.entity.UserEntity;
import com.example.booking.enums.EUserType;

public class UserFactory {

   public static UserResponse buildGuestUserResponse() {
      return UserResponse
            .builder()
            .id(1L)
            .username("usertest")
            .email("user@test.com")
            .type(EUserType.CUSTOMER)
            .build();
   }

   public static UserResponse buildPropertyOwnerUserResponse() {
      return UserResponse
            .builder()
            .id(1L)
            .username("usertest")
            .email("user@test.com")
            .type(EUserType.PROPERTY_OWNER)
            .build();
   }

   public static UserEntity buildGuestUser() {
      return UserEntity
            .builder()
            .id(1L)
            .type(EUserType.CUSTOMER)
            .email("user@test.com")
            .username("usertest")
            .build();
   }

   public static UserEntity buildPropertyOwnerUser() {
      return UserEntity
            .builder()
            .id(1L)
            .type(EUserType.PROPERTY_OWNER)
            .email("user@test.com")
            .username("usertest")
            .build();
   }
}
