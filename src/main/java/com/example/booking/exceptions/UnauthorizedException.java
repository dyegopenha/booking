package com.example.booking.exceptions;

import java.io.Serial;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends ApiException {

   @Serial
   private static final long serialVersionUID = 1L;

   public UnauthorizedException(String message) {
      super(HttpStatus.UNAUTHORIZED, message);
   }

   public UnauthorizedException() {
      super(HttpStatus.UNAUTHORIZED, "Unauthorized");
   }
}
