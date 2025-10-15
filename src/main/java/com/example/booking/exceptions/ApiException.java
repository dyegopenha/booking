package com.example.booking.exceptions;

import java.io.Serial;

import org.springframework.http.HttpStatus;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper=false)
public class ApiException extends RuntimeException {

   @Serial
   private static final long serialVersionUID = 1L;

   private HttpStatus status;
   private String message;

   public ApiException(HttpStatus status, String message) {
      super(message);
      this.status = status;
      this.message = message;
   }
}
