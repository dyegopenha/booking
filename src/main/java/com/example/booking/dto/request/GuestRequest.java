package com.example.booking.dto.request;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GuestRequest {

   @NotBlank
   private String fullName;

   @NotBlank
   private String documentNumber;

   @NotNull
   private LocalDate birthDate;

   @NotNull
   private Boolean mainGuest;
}
