package com.example.booking.factory;

import java.math.BigDecimal;

import com.example.booking.dto.response.RoomResponse;
import com.example.booking.entity.RoomEntity;

public class RoomFactory {

   public static RoomResponse buildRoomResponse(){
      return RoomResponse
            .builder()
            .id(1L)
            .property(PropertyFactory.buildPropertyResponse())
            .name("Room Test")
            .capacity(2)
            .dailyPrice(BigDecimal.valueOf(150))
            .build();
   }

   public static RoomEntity buildRoom(){
      return RoomEntity
            .builder()
            .id(1L)
            .name("Room Test")
            .capacity(2)
            .dailyPrice(BigDecimal.valueOf(150))
            .property(PropertyFactory.buildProperty())
            .build();
   }
}
