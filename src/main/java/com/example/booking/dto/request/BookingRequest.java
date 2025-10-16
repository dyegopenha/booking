package com.example.booking.dto.request;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BookingRequest {

   @NotNull
   private Long roomId;

   @NotNull
   private LocalDate startDate;

   @NotNull
   private LocalDate endDate;

   @NotNull
   private Integer guestQuantity;

   @NotNull
   private List<GuestRequest> guests;
}
