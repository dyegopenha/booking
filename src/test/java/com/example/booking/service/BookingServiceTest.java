package com.example.booking.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import com.example.booking.configuration.UserContext;
import com.example.booking.dto.request.BlockRequest;
import com.example.booking.dto.request.BookingRequest;
import com.example.booking.dto.response.BookingResponse;
import com.example.booking.entity.BookingEntity;
import com.example.booking.entity.RoomEntity;
import com.example.booking.entity.UserEntity;
import com.example.booking.enums.EBookingStatus;
import com.example.booking.enums.EPaymentStatus;
import com.example.booking.enums.EPropertyType;
import com.example.booking.exceptions.BadRequestException;
import com.example.booking.exceptions.ConflictException;
import com.example.booking.exceptions.NotFoundException;
import com.example.booking.factory.BookingFactory;
import com.example.booking.factory.RoomFactory;
import com.example.booking.factory.UserFactory;
import com.example.booking.repository.BookingRepository;
import com.example.booking.repository.GuestRepository;
import com.example.booking.repository.RoomRepository;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class BookingServiceTest {

   private final UserEntity expectedUser = UserFactory.buildGuestUser();
   private final RoomEntity expectedRoom = RoomFactory.buildRoom();
   private final BookingEntity expectedBooking = BookingFactory.buildBooking();
   private final BookingRequest validBookingRequest = BookingFactory.buildBookingRequest();
   private final BookingEntity expectedCanceledBooking = BookingFactory.buildCanceledBooking();
   private final BookingRequest invalidDatesBookingRequest = BookingFactory.buildInvalidDatesBookingRequest();
   private final BookingRequest invalidRoomCapacityBookingRequest = BookingFactory.buildInvalidRoomCapacityBookingRequest();
   private final BlockRequest validBlockRequest = BookingFactory.buildValidBlockRequest();
   private final BlockRequest invalidDatesBlockRequest = BookingFactory.buildInvalidDatesBlockRequest();

   @Mock
   private BookingRepository bookingRepository;

   @Mock
   private RoomRepository roomRepository;

   @Mock
   private GuestRepository guestRepository;

   @InjectMocks
   private BookingServiceImpl bookingService;

   @Test
   void whenCreateBookingThenCreateSuccessfully() {
      UserContext.getInstance().setUser(expectedUser);

      when(bookingRepository.findActiveBookingsForDates(anyLong(), any(LocalDate.class), any(LocalDate.class)))
            .thenReturn(Optional.empty());

      when(roomRepository.findById(anyLong())).thenReturn(Optional.of(expectedRoom));

      when(bookingRepository.save(any(BookingEntity.class))).thenReturn(expectedBooking);

      BookingResponse result = bookingService.createBooking(validBookingRequest);

      assertNotNull(result);
      assertEquals(expectedBooking.getId(), result.getId());
   }

   @Test
   void whenUpdateBookingThenUpdateSuccessfully() {
      UserContext.getInstance().setUser(expectedUser);

      when(bookingRepository.findActiveBookingsForDatesExcludingCurrent(
            anyLong(), any(LocalDate.class), any(LocalDate.class), any(Long.class)))
            .thenReturn(Optional.empty());

      when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(expectedBooking));

      when(roomRepository.findById(anyLong())).thenReturn(Optional.of(expectedRoom));

      when(bookingRepository.save(any(BookingEntity.class))).thenReturn(expectedBooking);

      BookingResponse result = bookingService.updateBooking(expectedBooking.getId(), validBookingRequest);

      assertNotNull(result);
      assertEquals(expectedBooking.getId(), result.getId());
   }

   @Test
   void whenRebookCanceledBookingThenRebookSuccessfully() {
      UserContext.getInstance().setUser(expectedUser);

      when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(expectedCanceledBooking));

      when(bookingRepository.findActiveBookingsForDatesExcludingCurrent(
            anyLong(), any(LocalDate.class), any(LocalDate.class), any(Long.class)))
            .thenReturn(Optional.empty());

      when(bookingRepository.save(any(BookingEntity.class))).thenReturn(expectedBooking);

      BookingResponse result = bookingService.rebookCanceledBooking(1L);

      assertNotNull(result);
      assertEquals(expectedBooking.getId(), result.getId());
      assertEquals(EBookingStatus.CONFIRMED, result.getStatus());
      assertEquals(EPaymentStatus.PAID, result.getPaymentStatus());
   }

   @Test
   void whenDeleteBookingThenDeleteSuccessfully() {
      UserContext.getInstance().setUser(expectedUser);

      when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(expectedCanceledBooking));

      doNothing().when(bookingRepository).delete(any(BookingEntity.class));

      bookingService.deleteBooking(1L);
   }

   @Test
   void whenCancelBookingThenCancelSuccessfully() {
      UserContext.getInstance().setUser(expectedUser);

      when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(expectedBooking));

      when(bookingRepository.save(any(BookingEntity.class))).thenReturn(expectedCanceledBooking);

      BookingResponse result = bookingService.cancelBooking(1L);

      assertNotNull(result);
      assertEquals(expectedBooking.getId(), result.getId());
      assertEquals(EBookingStatus.CANCELLED, result.getStatus());
      assertEquals(EPaymentStatus.REFUNDED, result.getPaymentStatus());
   }

   @Test
   void whenGetBookingByIdThenReturnBookingSuccessfully() {
      UserContext.getInstance().setUser(expectedUser);

      when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(expectedBooking));

      BookingResponse result = bookingService.getBookingById(1L);

      assertNotNull(result);
      assertEquals(expectedBooking.getId(), result.getId());
   }

   @Test
   void whenGetAllBookingsThenReturnSuccessfulResponse() {
      UserContext.getInstance().setUser(expectedUser);

      Page<BookingEntity> bookingPage = new PageImpl<>(List.of(expectedBooking));

      when(bookingRepository.findBookings(anyLong(), anyString(), anyString(), any(EPropertyType.class),
            any(EBookingStatus.class), any(Pageable.class))).thenReturn(bookingPage);

      Page<BookingResponse> result = bookingService.getAllBookings("test", "test",
            EPropertyType.HOTEL, EBookingStatus.CONFIRMED, 0, 10);

      assertNotNull(result);
      assertEquals(1, result.getTotalElements());
   }

   @Test
   void whenVerifyingEndAndStartDatesCreatingBlockThenThrowBadRequestException() {
      UserContext.getInstance().setUser(expectedUser);

      when(roomRepository.findById(anyLong())).thenReturn(Optional.of(expectedRoom));

      BadRequestException exception = assertThrows(BadRequestException.class, () -> {
         bookingService.createBooking(invalidDatesBookingRequest);
      });

      assertEquals("Start date needs to be before end date", exception.getMessage());
   }

   @Test
   void whenVerifyingExistingBookingBeforeCreatingThenThrowConflictException() {
      UserContext.getInstance().setUser(expectedUser);

      when(roomRepository.findById(anyLong())).thenReturn(Optional.of(expectedRoom));

      when(bookingRepository.findActiveBookingsForDates(anyLong(), any(LocalDate.class), any(LocalDate.class)))
            .thenReturn(Optional.of(Collections.singletonList(expectedBooking)));

      ConflictException exception = assertThrows(ConflictException.class, () -> {
         bookingService.createBooking(validBookingRequest);
      });

      assertEquals("Room is not available for the given dates", exception.getMessage());
   }

   @Test
   void whenFindingRoomByIdCreatingBlockThenThrowNotFoundException() {
      UserContext.getInstance().setUser(expectedUser);

      when(bookingRepository.findActiveBookingsForDates(anyLong(), any(LocalDate.class), any(LocalDate.class)))
            .thenReturn(Optional.empty());

      NotFoundException exception = assertThrows(NotFoundException.class, () -> {
         bookingService.createBooking(validBookingRequest);
      });

      assertEquals("Room not found", exception.getMessage());
   }

   @Test
   void whenVerifyingRoomCapacityThenThrowBadRequestException() {
      UserContext.getInstance().setUser(expectedUser);

      when(bookingRepository.findActiveBookingsForDates(anyLong(), any(LocalDate.class), any(LocalDate.class)))
            .thenReturn(Optional.empty());

      when(roomRepository.findById(anyLong())).thenReturn(Optional.of(expectedRoom));

      BadRequestException exception = assertThrows(BadRequestException.class, () -> {
         bookingService.createBooking(invalidRoomCapacityBookingRequest);
      });

      assertEquals("Room does not support the requested amount of guests", exception.getMessage());
   }

   @Test
   void whenVerifyingExistingBookingBeforeUpdatingThenThrowConflictException() {
      UserContext.getInstance().setUser(expectedUser);

      when(bookingRepository.findActiveBookingsForDatesExcludingCurrent(anyLong(), any(LocalDate.class),
            any(LocalDate.class), any(Long.class))).thenReturn(Optional.of(Collections.singletonList(expectedBooking)));

      ConflictException exception = assertThrows(ConflictException.class, () -> {
         bookingService.updateBooking(1L, validBookingRequest);
      });

      assertEquals("Room is not available for the given dates", exception.getMessage());
   }

   @Test
   void whenCreateBlockThenCreateSuccessfully() {
      UserContext.getInstance().setUser(expectedUser);

      when(bookingRepository.findActiveBookingsForDatesExcludingCurrent(anyLong(), any(LocalDate.class),
            any(LocalDate.class), anyLong())).thenReturn(Optional.empty());

      when(roomRepository.findById(anyLong())).thenReturn(Optional.of(expectedRoom));

      when(bookingRepository.save(any(BookingEntity.class))).thenReturn(expectedBooking);

      BookingResponse result = bookingService.createBlock(validBlockRequest);

      assertNotNull(result);
      assertEquals(expectedBooking.getId(), result.getId());
   }

   @Test
   void whenUpdateBlockThenUpdateBlockSuccessfully() {
      UserContext.getInstance().setUser(expectedUser);

      when(bookingRepository.findActiveBookingsForDatesExcludingCurrent(
            anyLong(), any(LocalDate.class), any(LocalDate.class), any(Long.class)))
            .thenReturn(Optional.empty());

      when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(expectedBooking));

      when(roomRepository.findById(anyLong())).thenReturn(Optional.of(expectedRoom));

      when(bookingRepository.save(any(BookingEntity.class))).thenReturn(expectedBooking);

      BookingResponse result = bookingService.updateBlock(expectedBooking.getId(), validBlockRequest);

      assertNotNull(result);
      assertEquals(expectedBooking.getId(), result.getId());
   }

   @Test
   void whenVerifyingEndAndStartDatesThenThrowBadRequestException() {
      UserContext.getInstance().setUser(expectedUser);

      BadRequestException exception = assertThrows(BadRequestException.class, () -> {
         bookingService.createBlock(invalidDatesBlockRequest);
      });

      assertEquals("Start date needs to be before end date", exception.getMessage());
   }

   @Test
   void whenVerifyingExistingBookingBeforeCreatingBlockThenThrowConflictException() {
      UserContext.getInstance().setUser(expectedUser);

      when(roomRepository.findById(anyLong())).thenReturn(Optional.of(expectedRoom));

      when(bookingRepository.findActiveBookingsForDates(anyLong(), any(LocalDate.class), any(LocalDate.class)))
            .thenReturn(Optional.of(Collections.singletonList(expectedBooking)));

      ConflictException exception = assertThrows(ConflictException.class, () -> {
         bookingService.createBlock(validBlockRequest);
      });

      assertEquals("Room is not available for the given dates", exception.getMessage());
   }

   @Test
   void whenFindingRoomByIdThenThrowNotFoundException() {
      UserContext.getInstance().setUser(expectedUser);

      when(bookingRepository.findActiveBookingsForDates(anyLong(), any(LocalDate.class), any(LocalDate.class)))
            .thenReturn(Optional.empty());

      NotFoundException exception = assertThrows(NotFoundException.class, () -> {
         bookingService.createBlock(validBlockRequest);
      });

      assertEquals("Room not found", exception.getMessage());
   }

   @Test
   void whenFindingBookingByIdThenThrowNotFoundException() {
      UserContext.getInstance().setUser(expectedUser);

      when(roomRepository.findById(anyLong())).thenReturn(Optional.of(expectedRoom));

      when(bookingRepository.findActiveBookingsForDatesExcludingCurrent(anyLong(), any(LocalDate.class),
            any(LocalDate.class), anyLong())).thenReturn(Optional.empty());

      NotFoundException exception = assertThrows(NotFoundException.class, () -> {
         bookingService.updateBlock(99L, validBlockRequest);
      });

      assertEquals("Booking not found", exception.getMessage());
   }

   @Test
   void whenVerifyingExistingBlockBeforeUpdatingThenThrowConflictException() {
      UserContext.getInstance().setUser(expectedUser);

      when(roomRepository.findById(anyLong())).thenReturn(Optional.of(expectedRoom));

      when(bookingRepository.findActiveBookingsForDatesExcludingCurrent(anyLong(), any(LocalDate.class),
            any(LocalDate.class), any(Long.class))).thenReturn(Optional.of(Collections.singletonList(expectedBooking)));

      ConflictException exception = assertThrows(ConflictException.class, () -> {
         bookingService.updateBlock(1L, validBlockRequest);
      });

      assertEquals("Room is not available for the given dates", exception.getMessage());
   }
}
