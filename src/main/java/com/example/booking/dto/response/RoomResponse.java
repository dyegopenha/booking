package com.example.booking.dto.response;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import com.example.booking.entity.RoomEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponse {
   private Long id;
   private PropertyResponse property;
   private String name;
   private Integer capacity;
   private BigDecimal dailyPrice;

   public static RoomResponse from(RoomEntity r) {
      return RoomResponse
            .builder()
            .id(r.getId())
            .property(PropertyResponse.from(r.getProperty()))
            .name(r.getName())
            .capacity(r.getCapacity())
            .dailyPrice(r.getDailyPrice())
            .build();
   }

   public static List<RoomResponse> from(List<RoomEntity> rooms) {
      if (rooms == null) {
         return Collections.emptyList();
      }
      return rooms.stream().map(RoomResponse::from).toList();
   }
}
