package com.example.booking.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.booking.dto.response.RoomResponse;
import com.example.booking.entity.RoomEntity;
import com.example.booking.enums.EPropertyType;
import com.example.booking.repository.RoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements IRoomService {

   private final RoomRepository roomRepository;

   @Override
   public Page<RoomResponse> findAvailableRooms(String propertyName, String roomName, EPropertyType propertyType, Integer guestQuantity,
         LocalDate startDate, LocalDate endDate, int page, int size) {
      Pageable pageable = PageRequest.of(page, size);
      Page<RoomEntity> rooms = roomRepository.findAvailableRooms(
            propertyName, roomName, propertyType, guestQuantity,
            startDate, endDate, pageable);
      return rooms.map(RoomResponse::from);
   }
}
