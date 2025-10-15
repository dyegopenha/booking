package com.example.booking.factory;

import com.example.booking.dto.response.PropertyResponse;
import com.example.booking.entity.PropertyEntity;
import com.example.booking.enums.EPropertyType;

public class PropertyFactory {

   public static PropertyResponse buildPropertyResponse(){
      return PropertyResponse
            .builder()
            .id(1L)
            .name("Property Test")
            .type(EPropertyType.HOUSE)
            .user(UserFactory.buildPropertyOwnerUserResponse())
            .build();
   }

   public static PropertyEntity buildProperty(){
      return PropertyEntity
            .builder()
            .id(1L)
            .user(UserFactory.buildPropertyOwnerUser())
            .type(EPropertyType.HOUSE)
            .name("Property Test")
            .build();
   }
}
