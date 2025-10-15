package com.example.booking.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BlockRequest {

   @NotNull
   private Long roomId;

   @NotNull
   private LocalDate startDate;

   @NotNull
   private LocalDate endDate;
}
