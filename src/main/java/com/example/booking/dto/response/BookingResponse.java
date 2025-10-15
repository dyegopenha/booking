package com.example.booking.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import com.example.booking.entity.BookingEntity;
import com.example.booking.enums.EBookingStatus;
import com.example.booking.enums.EBookingType;
import com.example.booking.enums.EPaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
   private Long id;
   private UserResponse user;
   private RoomResponse room;
   private EBookingStatus status;
   private EBookingType type;
   private Integer guestQuantity;
   private LocalDate startDate;
   private LocalDate endDate;
   private BigDecimal totalPrice;
   private EPaymentStatus paymentStatus;
   private List<GuestResponse> guests;

   public static BookingResponse from(BookingEntity b) {
      return BookingResponse
            .builder()
            .id(b.getId())
            .user(UserResponse.from(b.getUser()))
            .room(RoomResponse.from(b.getRoom()))
            .status(b.getStatus())
            .type(b.getType())
            .guestQuantity(b.getGuestQuantity())
            .startDate(b.getStartDate())
            .endDate(b.getEndDate())
            .totalPrice(b.getTotalPrice())
            .paymentStatus(b.getPaymentStatus())
            .guests(GuestResponse.from(b.getGuests()))
            .build();
   }

   public static List<BookingResponse> from(List<BookingEntity> bookings) {
      if (bookings == null) {
         return Collections.emptyList();
      }
      return bookings.stream().map(BookingResponse::from).toList();
   }
}
