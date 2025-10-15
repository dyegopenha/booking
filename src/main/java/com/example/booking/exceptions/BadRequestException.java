package com.example.booking.exceptions;

import java.io.Serial;

import org.springframework.http.HttpStatus;

public class BadRequestException extends ApiException {

   @Serial
   private static final long serialVersionUID = 1L;

   public BadRequestException(String message) {
      super(HttpStatus.BAD_REQUEST, message);
   }

   public BadRequestException() {
      super(HttpStatus.BAD_REQUEST, "Bad Request");
   }
}
