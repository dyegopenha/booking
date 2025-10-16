package com.example.booking.dto.response;

import java.util.Collections;
import java.util.List;

import com.example.booking.entity.UserEntity;
import com.example.booking.enums.EUserType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserResponse {
   private Long id;
   private String username;
   private String email;
   private EUserType type;

   public static UserResponse from(UserEntity u) {
      return UserResponse
            .builder()
            .id(u.getId())
            .username(u.getUsername())
            .email(u.getEmail())
            .type(u.getType())
            .build();
   }

   public static List<UserResponse> from(List<UserEntity> users) {
      if (users == null) {
         return Collections.emptyList();
      }
      return users.stream().map(UserResponse::from).toList();
   }
}
