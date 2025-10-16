package com.example.booking.dto.response;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import com.example.booking.entity.GuestEntity;
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
public class GuestResponse {
   private Long id;
   private String fullName;
   private String documentNumber;
   private LocalDate birthDate;
   private Boolean mainGuest;

   public static GuestResponse from(GuestEntity g) {
      return GuestResponse
            .builder()
            .id(g.getId())
            .fullName(g.getFullName())
            .documentNumber(g.getDocumentNumber())
            .birthDate(g.getBirthDate())
            .mainGuest(g.getMainGuest())
            .build();
   }

   public static List<GuestResponse> from(List<GuestEntity> guests) {
      if (guests == null) {
         return Collections.emptyList();
      }
      return guests.stream().map(GuestResponse::from).toList();
   }
}
