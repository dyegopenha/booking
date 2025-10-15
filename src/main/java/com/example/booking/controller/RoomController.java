package com.example.booking.controller;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.booking.dto.response.RoomResponse;
import com.example.booking.enums.EPropertyType;
import com.example.booking.service.IRoomService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/rooms")
@RequiredArgsConstructor
public class RoomController {

   private final IRoomService roomService;

   @GetMapping
   public ResponseEntity<Page<RoomResponse>> getAvailableRooms(
         @RequestParam(required = false) String propertyName,
         @RequestParam(required = false) String roomName,
         @RequestParam(required = false) EPropertyType propertyType,
         @RequestParam(required = false) Integer guestQuantity,
         @RequestParam LocalDate startDate,
         @RequestParam LocalDate endDate,
         @RequestParam(defaultValue = "0") int page,
         @RequestParam(defaultValue = "10") int size) {

      Page<RoomResponse> rooms = roomService.findAvailableRooms(
            propertyName, roomName, propertyType, guestQuantity,
            startDate, endDate, page, size);

      return ResponseEntity.ok(rooms);
   }
}
