package com.example.booking.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.booking.enums.EBookingStatus;
import com.example.booking.enums.EBookingType;
import com.example.booking.enums.EPaymentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "bookings")
public class BookingEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(nullable = false)
   private Long userId;

   @Column(nullable = false)
   private Long roomId;

   @Enumerated(EnumType.STRING)
   @Column(nullable = false)
   private EBookingStatus status;

   @Enumerated(EnumType.STRING)
   @Column(nullable = false)
   private EBookingType type;

   @Column(nullable = false)
   private Integer guestQuantity;

   @Column(nullable = false)
   private LocalDate startDate;

   @Column(nullable = false)
   private LocalDate endDate;

   @Column(nullable = false)
   private BigDecimal totalPrice;

   @Enumerated(EnumType.STRING)
   @Column(nullable = false)
   private EPaymentStatus paymentStatus;
}
