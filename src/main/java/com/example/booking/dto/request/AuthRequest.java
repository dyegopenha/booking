package com.example.booking.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthRequest {

   @NotBlank
   private String username;

   @NotBlank
   private String password;
}
