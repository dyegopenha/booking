package com.example.booking.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;

import com.example.booking.dto.response.RoomResponse;
import com.example.booking.enums.EPropertyType;

public interface IRoomService {
   Page<RoomResponse> findAvailableRooms(
         String propertyName, String roomName, EPropertyType propertyType,
         Integer guestQuantity, LocalDate startDate, LocalDate endDate,
         int page, int size);
}
