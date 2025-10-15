package com.example.booking.dto.response;

import java.util.Collections;
import java.util.List;

import com.example.booking.entity.PropertyEntity;
import com.example.booking.enums.EPropertyType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyResponse {
   private Long id;
   private String name;
   private EPropertyType type;
   private UserResponse user;

   public static PropertyResponse from(PropertyEntity p) {
      return PropertyResponse
            .builder()
            .id(p.getId())
            .name(p.getName())
            .type(p.getType())
            .user(UserResponse.from(p.getUser()))
            .build();
   }

   public static List<PropertyResponse> from(List<PropertyEntity> properties) {
      if (properties == null) {
         return Collections.emptyList();
      }
      return properties.stream().map(PropertyResponse::from).toList();
   }
}
