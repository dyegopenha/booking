package com.example.booking.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.booking.dto.request.BlockRequest;
import com.example.booking.dto.request.BookingRequest;
import com.example.booking.dto.response.BookingResponse;
import com.example.booking.enums.EBookingStatus;
import com.example.booking.enums.EPropertyType;
import com.example.booking.service.IBookingService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

   private final IBookingService bookingService;

   @PostMapping
   public ResponseEntity<BookingResponse> createBooking(@RequestBody @Valid BookingRequest request) {
      return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.createBooking(request));
   }

   @PutMapping("{id}")
   public ResponseEntity<BookingResponse> updateBooking(@PathVariable Long id, @RequestBody @Valid BookingRequest request) {
      return ResponseEntity.ok(bookingService.updateBooking(id, request));
   }

   @PutMapping("{id}/rebook")
   public ResponseEntity<BookingResponse> rebookCanceledBooking(@PathVariable Long id) {
      return ResponseEntity.ok(bookingService.rebookCanceledBooking(id));
   }

   @DeleteMapping("{id}")
   public ResponseEntity<Void> deleteBooking(@PathVariable Long id){
      bookingService.deleteBooking(id);
      return ResponseEntity.noContent().build();
   }
   @PutMapping("{id}/cancel")
   public ResponseEntity<BookingResponse> cancelBooking(@PathVariable Long id){
      return ResponseEntity.ok(bookingService.cancelBooking(id));
   }

   @GetMapping("{id}")
   public ResponseEntity<BookingResponse> getBookingById(@PathVariable Long id){
      return ResponseEntity.ok(bookingService.getBookingById(id));
   }

   @GetMapping
   public ResponseEntity<Page<BookingResponse>> getAllBookings(
         @RequestParam(required = false) String propertyName,
         @RequestParam(required = false) String roomName,
         @RequestParam(required = false) EPropertyType propertyType,
         @RequestParam(required = false) EBookingStatus bookingStatus,
         @RequestParam(defaultValue = "0") int page,
         @RequestParam(defaultValue = "10") int size) {

      return ResponseEntity.ok(bookingService.getAllBookings(
            propertyName, roomName, propertyType, bookingStatus, page, size)
      );
   }

   @PostMapping("blocks")
   public ResponseEntity<BookingResponse> createBlock(@RequestBody @Valid BlockRequest request){
      return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.createBlock(request));
   }

   @PutMapping("blocks/{id}")
   public ResponseEntity<BookingResponse> updateBookingBlock(@PathVariable Long id,
         @RequestBody @Valid BlockRequest request){
      return ResponseEntity.ok(bookingService.updateBlock(id, request));
   }
}
