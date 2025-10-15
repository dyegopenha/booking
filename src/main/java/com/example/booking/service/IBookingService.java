package com.example.booking.service;

import org.springframework.data.domain.Page;

import com.example.booking.dto.request.BlockRequest;
import com.example.booking.dto.request.BookingRequest;
import com.example.booking.dto.response.BookingResponse;
import com.example.booking.enums.EBookingStatus;
import com.example.booking.enums.EPropertyType;

public interface IBookingService {

   BookingResponse createBooking(BookingRequest request);
   BookingResponse updateBooking(Long bookingId, BookingRequest request);
   BookingResponse rebookCanceledBooking(Long id);
   void deleteBooking(Long id);
   BookingResponse cancelBooking(Long id);
   BookingResponse getBookingById(Long id);
   Page<BookingResponse> getAllBookings(
         String propertyName, String roomName, EPropertyType propertyType,
         EBookingStatus bookingStatus, int page, int size);
   BookingResponse createBlock(BlockRequest request);
   BookingResponse updateBlock(Long bookingId, BlockRequest request);
}
