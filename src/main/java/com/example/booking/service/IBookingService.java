package com.example.booking.service;

import org.springframework.data.domain.Page;

import com.example.booking.dto.request.BlockRequest;
import com.example.booking.dto.request.BookingRequest;
import com.example.booking.dto.response.BookingResponse;
import com.example.booking.enums.EBookingStatus;
import com.example.booking.enums.EPropertyType;

public interface IBookingService {

   BookingResponse createBooking(BookingRequest request, Long userId);
   BookingResponse updateBooking(Long bookingId, BookingRequest request, Long userId);
   BookingResponse rebookCanceledBooking(Long id, Long userId);
   void deleteBooking(Long id, Long userId);
   BookingResponse cancelBooking(Long id, Long userId);
   BookingResponse getBookingById(Long id, Long userId);
   Page<BookingResponse> getAllBookings(
         String propertyName, String roomName, EPropertyType propertyType,
         EBookingStatus bookingStatus, int page, int size, Long userId);
   BookingResponse createBlock(BlockRequest request, Long userId);
   BookingResponse updateBlock(Long bookingId, BlockRequest request, Long userId);
}
