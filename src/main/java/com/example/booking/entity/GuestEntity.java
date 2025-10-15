package com.example.booking.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "guests")
public class GuestEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(nullable = false)
   private Long booking_id;

   @Column(nullable = false)
   private String fullName;

   @Column(nullable = false)
   private String documentNumber;

   @Column(nullable = false)
   private LocalDate birthDate;

   @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
   private Boolean mainGuest;
}
