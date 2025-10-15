package com.example.booking.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import com.example.booking.dto.response.RoomResponse;
import com.example.booking.entity.RoomEntity;
import com.example.booking.entity.UserEntity;
import com.example.booking.enums.EPropertyType;
import com.example.booking.factory.RoomFactory;
import com.example.booking.factory.UserFactory;
import com.example.booking.repository.RoomRepository;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class RoomServiceTest {

   @Mock
   private RoomRepository roomRepository;

   @InjectMocks
   private RoomServiceImpl roomService;

   @Test
   void whenFindAvailableRoomsThenReturnSuccessfulResponse() {
      UserEntity expectedUser = UserFactory.buildGuestUser();
      RoomEntity expectedRoom = RoomFactory.buildRoom();
      
      Page<RoomEntity> roomPage = new PageImpl<>(List.of(expectedRoom));

      when(roomRepository.findAvailableRooms(anyString(), anyString(), any(EPropertyType.class),
            anyInt(), any(LocalDate.class), any(LocalDate.class), any(Pageable.class)))
            .thenReturn(roomPage);

      Page<RoomResponse> result = roomService.findAvailableRooms("test", "test",
            EPropertyType.HOTEL, 2, LocalDate.of(2025, 4, 18),
            LocalDate.of(2025, 4, 22), 0, 10);

      assertNotNull(result);
      assertEquals(1, result.getTotalElements());
   }
}
