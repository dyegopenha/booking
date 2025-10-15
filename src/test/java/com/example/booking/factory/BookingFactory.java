package com.example.booking.factory;

import java.time.LocalDate;

import com.example.booking.dto.request.BlockRequest;
import com.example.booking.dto.request.BookingRequest;
import com.example.booking.dto.response.BookingResponse;
import com.example.booking.entity.BookingEntity;
import com.example.booking.enums.EBookingStatus;
import com.example.booking.enums.EBookingType;
import com.example.booking.enums.EPaymentStatus;

public class BookingFactory {
   public static BookingResponse buildBookingResponse(){
      return BookingResponse
            .builder()
            .id(1L)
            .type(EBookingType.GUEST)
            .startDate(LocalDate.of(2025, 10, 19))
            .endDate(LocalDate.of(2025, 10, 23))
            .room(RoomFactory.buildRoomResponse())
            .user(UserFactory.buildGuestUserResponse())
            .guests(GuestFactory.buildGuestResponseList())
            .status(EBookingStatus.CONFIRMED)
            .paymentStatus(EPaymentStatus.PAID)
            .build();
   }

   public static BookingResponse buildCancelledBookingResponse(){
      return BookingResponse
            .builder()
            .id(1L)
            .type(EBookingType.GUEST)
            .startDate(LocalDate.of(2025, 10, 19))
            .endDate(LocalDate.of(2025, 10, 23))
            .room(RoomFactory.buildRoomResponse())
            .user(UserFactory.buildGuestUserResponse())
            .guests(GuestFactory.buildGuestResponseList())
            .status(EBookingStatus.CANCELLED)
            .paymentStatus(EPaymentStatus.REFUNDED)
            .build();
   }

   public static BookingRequest buildBookingRequest(){
      return BookingRequest
            .builder()
            .roomId(1L)
            .startDate(LocalDate.of(2025, 10, 19))
            .endDate(LocalDate.of(2025, 10, 23))
            .guestQuantity(2)
            .guests(GuestFactory.buildGuestRequestList())
            .build();
   }

   public static BookingRequest buildInvalidDatesBookingRequest(){
      return BookingRequest
            .builder()
            .roomId(1L)
            .startDate(LocalDate.of(2025, 10, 23))
            .endDate(LocalDate.of(2025, 10, 19))
            .guestQuantity(2)
            .guests(GuestFactory.buildGuestRequestList())
            .build();
   }

   public static BookingRequest buildInvalidRoomCapacityBookingRequest(){
      return BookingRequest
            .builder()
            .roomId(1L)
            .startDate(LocalDate.of(2025, 10, 19))
            .endDate(LocalDate.of(2025, 10, 23))
            .guestQuantity(50)
            .guests(GuestFactory.buildGuestRequestList())
            .build();
   }

   public static BookingRequest buildUpdateBookingRequest(){
      return BookingRequest
            .builder()
            .roomId(1L)
            .startDate(LocalDate.of(2025, 12, 19))
            .endDate(LocalDate.of(2025, 12, 23))
            .guestQuantity(2)
            .guests(GuestFactory.buildGuestRequestList())
            .build();
   }

   public static BookingEntity buildBooking(){
      return BookingEntity
            .builder()
            .id(1L)
            .type(EBookingType.GUEST)
            .startDate(LocalDate.of(2025, 10, 19))
            .endDate(LocalDate.of(2025, 10, 23))
            .guests(GuestFactory.buildGuest())
            .room(RoomFactory.buildRoom())
            .user(UserFactory.buildGuestUser())
            .status(EBookingStatus.CONFIRMED)
            .paymentStatus(EPaymentStatus.PAID)
            .build();
   }

   public static BookingEntity buildCanceledBooking(){
      return BookingEntity
            .builder()
            .id(1L)
            .type(EBookingType.GUEST)
            .startDate(LocalDate.of(2025, 10, 19))
            .endDate(LocalDate.of(2025, 10, 23))
            .guests(GuestFactory.buildGuest())
            .room(RoomFactory.buildRoom())
            .user(UserFactory.buildGuestUser())
            .status(EBookingStatus.CANCELLED)
            .paymentStatus(EPaymentStatus.REFUNDED)
            .build();
   }

   public static BlockRequest buildValidBlockRequest(){
      return BlockRequest
            .builder()
            .roomId(1L)
            .startDate(LocalDate.of(2026, 2, 19))
            .endDate(LocalDate.of(2026, 2, 23))
            .build();
   }

   public static BlockRequest buildInvalidDatesBlockRequest(){
      return BlockRequest
            .builder()
            .roomId(1L)
            .startDate(LocalDate.of(2026, 2, 23))
            .endDate(LocalDate.of(2026, 2, 19))
            .build();
   }

   public static BlockRequest buildValidUpdateBlockRequest(){
      return BlockRequest
            .builder()
            .roomId(1L)
            .startDate(LocalDate.of(2026, 3, 19))
            .endDate(LocalDate.of(2026, 3, 23))
            .build();
   }

}
