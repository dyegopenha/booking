package com.example.booking.exceptions;

import java.io.Serial;

import org.springframework.http.HttpStatus;

public class ConflictException extends ApiException {

   @Serial
   private static final long serialVersionUID = 1L;

   public ConflictException(String message) {
      super(HttpStatus.CONFLICT, message);
   }

   public ConflictException() {
      super(HttpStatus.CONFLICT, "Conflict");
   }
}
