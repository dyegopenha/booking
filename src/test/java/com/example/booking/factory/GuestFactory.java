package com.example.booking.factory;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import com.example.booking.dto.request.GuestRequest;
import com.example.booking.dto.response.GuestResponse;
import com.example.booking.entity.GuestEntity;

public class GuestFactory {

   public static List<GuestResponse> buildGuestResponseList(){
      return GuestResponse.from(buildGuest());

   }

   public static List<GuestRequest> buildGuestRequestList(){
      return Arrays.asList(
            GuestRequest
                  .builder()
                  .fullName("Joao Silva")
                  .documentNumber("A12345")
                  .birthDate(LocalDate.of(1992,8,6))
                  .mainGuest(true)
                  .build(),
            GuestRequest
                  .builder()
                  .fullName("Maria Silva")
                  .documentNumber("B12345")
                  .birthDate(LocalDate.of(1994,4,30))
                  .mainGuest(false)
                  .build());
   }

   public static List<GuestEntity> buildGuest(){
      return Arrays.asList(
            GuestEntity
                  .builder()
                  .id(1L)
                  .mainGuest(Boolean.TRUE)
                  .birthDate(LocalDate.of(1992,8,6))
                  .fullName("Joao Silva")
                  .documentNumber("A12345")
                  .build(),
            GuestEntity
                  .builder()
                  .id(2L)
                  .mainGuest(Boolean.FALSE)
                  .birthDate(LocalDate.of(1994,4,30))
                  .fullName("Maria Silva")
                  .documentNumber("B12345")
                  .build());
   }
}
