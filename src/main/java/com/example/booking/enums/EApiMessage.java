package com.example.booking.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EApiMessage {

   ROOM_NOT_FOUND( "Room not found"),
   ROOM_NOT_AVAILABLE("Room is not available for the given dates"),
   ROOM_BAD_CAPACITY("Room does not support the requested amount of guests"),
   BOOKING_BAD_DATES("Start date needs to be before end date"),
   BOOKING_NOT_FOUND("Booking not found"),
   BOOKING_UNAUTHORIZED("You do not have permission to manage this booking"),
   BOOKING_BAD_DELETE("Cannot delete an active Booking");

   private String label;
}
