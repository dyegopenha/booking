package com.example.booking.exceptions;

import java.io.Serial;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ApiException {

   @Serial
   private static final long serialVersionUID = 1L;

   public NotFoundException(String message) {
      super(HttpStatus.NOT_FOUND, message);
   }

   public NotFoundException() {
      super(HttpStatus.NOT_FOUND, "Not Found");
   }
}
