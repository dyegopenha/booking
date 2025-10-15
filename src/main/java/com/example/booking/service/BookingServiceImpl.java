package com.example.booking.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.example.booking.configuration.UserContext;
import com.example.booking.dto.request.BlockRequest;
import com.example.booking.dto.request.BookingRequest;
import com.example.booking.dto.response.BookingResponse;
import com.example.booking.entity.BookingEntity;
import com.example.booking.entity.GuestEntity;
import com.example.booking.entity.RoomEntity;
import com.example.booking.entity.UserEntity;
import com.example.booking.enums.EBookingStatus;
import com.example.booking.enums.EBookingType;
import com.example.booking.enums.EPaymentStatus;
import com.example.booking.enums.EPropertyType;
import com.example.booking.exceptions.BadRequestException;
import com.example.booking.exceptions.ConflictException;
import com.example.booking.exceptions.NotFoundException;
import com.example.booking.exceptions.UnauthorizedException;
import com.example.booking.repository.BookingRepository;
import com.example.booking.repository.GuestRepository;
import com.example.booking.repository.RoomRepository;
import com.example.booking.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements IBookingService {

   private final BookingRepository bookingRepository;

   private final RoomRepository roomRepository;

   private final GuestRepository guestRepository;

   private final UserRepository userRepository;

   @Override
   @Transactional
   public BookingResponse createBooking(BookingRequest request) {
      RoomEntity room = getRoom(request.getRoomId());
      validateDates(request.getStartDate(), request.getEndDate());
      validateRoomCapacity(room, request.getGuestQuantity());
      checkRoomAvailability(request.getRoomId(), request.getStartDate(), request.getEndDate());
      BigDecimal totalPrice = calculateTotalPrice(request, room);
      BookingEntity savedBooking = saveBooking(request, room, totalPrice);
      List<GuestEntity> guests = saveGuests(request, savedBooking);
      savedBooking.setGuests(guests);
      room.setLastBookingAt(LocalDateTime.now());
      roomRepository.save(room);
      return BookingResponse.from(savedBooking);
   }

   @Override
   @Transactional
   public BookingResponse updateBooking(Long bookingId, BookingRequest request) {
      validateDates(request.getStartDate(), request.getEndDate());
      checkRoomAvailabilityForUpdateBooking(bookingId, request.getRoomId(), request.getStartDate(), request.getEndDate());
      RoomEntity room = getRoom(request.getRoomId());
      validateRoomCapacity(room, request.getGuestQuantity());
      BigDecimal totalPrice = calculateTotalPrice(request, room);
      BookingEntity updatedBooking = updateBooking(getBooking(bookingId), request, room, totalPrice);
      List<GuestEntity> guests = saveGuests(request, updatedBooking);
      updatedBooking.setGuests(guests);
      return BookingResponse.from(bookingRepository.save(updatedBooking));
   }

   @Override
   @Transactional
   public BookingResponse rebookCanceledBooking(Long id) {
      BookingEntity booking = getBooking(id);
      checkRoomAvailabilityForUpdateBooking(id, booking.getRoom().getId(), booking.getStartDate(), booking.getEndDate());
      booking.setStatus(EBookingStatus.CONFIRMED);
      booking.setPaymentStatus(EPaymentStatus.PAID);
      return BookingResponse.from(bookingRepository.save(booking));
   }

   @Override
   @Transactional
   public void deleteBooking(Long id) {
      BookingEntity booking = getBooking(id);
      validateOwnership(booking.getRoom());
      validateStatusForDeletion(booking);
      bookingRepository.delete(booking);
   }

   @Override
   @Transactional
   public BookingResponse cancelBooking(Long id) {
      BookingEntity booking = getBooking(id);
      booking.setStatus(EBookingStatus.CANCELLED);
      booking.setPaymentStatus(EPaymentStatus.REFUNDED);
      return BookingResponse.from(bookingRepository.save(booking));
   }

   @Override
   @Transactional(readOnly = true)
   public BookingResponse getBookingById(Long id) {
      return BookingResponse.from(getBooking(id));
   }

   @Override
   @Transactional(readOnly = true)
   public Page<BookingResponse> getAllBookings(String propertyName, String roomName, EPropertyType propertyType,
         EBookingStatus bookingStatus, int page, int size) {
      Pageable pageable = PageRequest.of(page, size);
      Page<BookingEntity> bookings = bookingRepository.findBookings(getLoggedUser().getId(),
            propertyName, roomName, propertyType, bookingStatus, pageable);
      return bookings.map(BookingResponse::from);
   }

   @Override
   @Transactional
   public BookingResponse createBlock(BlockRequest request) {
      validateDates(request.getStartDate(), request.getEndDate());
      RoomEntity room = getRoom(request.getRoomId());
      validateOwnership(room);
      checkRoomAvailability(request.getRoomId(), request.getStartDate(), request.getEndDate());
      BookingEntity savedBlock = saveBlock(request, room);
      return BookingResponse.from(savedBlock);
   }

   @Override
   @Transactional
   public BookingResponse updateBlock(Long bookingId, BlockRequest request) {
      validateDates(request.getStartDate(), request.getEndDate());
      RoomEntity room = getRoom(request.getRoomId());
      validateOwnership(room);
      checkRoomAvailabilityForUpdateBooking(bookingId, request.getRoomId(), request.getStartDate(), request.getEndDate());
      BookingEntity updatedBooking = updateBlock(bookingId, request, room);
      return BookingResponse.from(updatedBooking);
   }

   private RoomEntity getRoom(Long roomId) {
      return roomRepository.findById(roomId).orElseThrow(
            () -> new NotFoundException("Room not found")
      );
   }

   private void validateDates(LocalDate startDate, LocalDate endDate) {
      if (endDate.isBefore(startDate)) {
         throw new BadRequestException("Start date needs to be before end date");
      }
   }

   private void checkRoomAvailability(Long roomId, LocalDate startDate, LocalDate endDate) {
      Optional<List<BookingEntity>> existingBookings = bookingRepository.findActiveBookingsForDates(
            roomId,
            startDate,
            endDate
      );

      if (existingBookings.isPresent() && !existingBookings.get().isEmpty()) {
         throw new ConflictException("Room is not available for the given dates");
      }
   }

   private void validateRoomCapacity(RoomEntity room, int guestQuantity) {
      if (room.getCapacity() < guestQuantity) {
         throw new BadRequestException("Room does not support the requested amount of guests");
      }
   }

   private BigDecimal calculateTotalPrice(BookingRequest request, RoomEntity room) {
      long days = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate());
      return room.getDailyPrice().multiply(BigDecimal.valueOf(days));
   }

   private BookingEntity saveBooking(BookingRequest request, RoomEntity room, BigDecimal totalPrice) {
      BookingEntity booking = BookingEntity.builder()
                                           .type(EBookingType.GUEST)
                                           .user(getLoggedUser())
                                           .startDate(request.getStartDate())
                                           .endDate(request.getEndDate())
                                           .room(room)
                                           .paymentStatus(EPaymentStatus.PAID)
                                           .guestQuantity(request.getGuestQuantity())
                                           .totalPrice(totalPrice)
                                           .status(EBookingStatus.CONFIRMED)
                                           .build();
      return bookingRepository.save(booking);
   }

   private List<GuestEntity> saveGuests(BookingRequest request, BookingEntity savedBooking) {
      if(!CollectionUtils.isEmpty(savedBooking.getGuests())){
         guestRepository.deleteAll(savedBooking.getGuests());
      }

      List<GuestEntity> guests = request.getGuests().stream()
                                        .map(guest -> GuestEntity.builder()
                                                                 .fullName(guest.getFullName())
                                                                 .documentNumber(guest.getDocumentNumber())
                                                                 .birthDate(guest.getBirthDate())
                                                                 .mainGuest(guest.getMainGuest())
                                                                 .booking(savedBooking)
                                                                 .build())
                                        .toList();

      guestRepository.saveAll(guests);
      return guests;
   }

   private void checkRoomAvailabilityForUpdateBooking(Long bookingId, Long roomId, LocalDate startDate, LocalDate endDate) {
      Optional<List<BookingEntity>> existingBookings = bookingRepository.findActiveBookingsForDatesExcludingCurrent(
            roomId,
            startDate,
            endDate,
            bookingId
      );

      if (existingBookings.isPresent() && !existingBookings.get().isEmpty()) {
         throw new ConflictException("Room is not available for the given dates");
      }
   }

   private BookingEntity getBooking(Long id){
      BookingEntity booking = bookingRepository.findById(id).orElseThrow(() -> new NotFoundException("Booking not found"));

      if (isPropertyOwner(booking) || isBookingUser(booking)) {
         return booking;
      }
      throw new NotFoundException("Booking not found");
   }

   private Boolean isPropertyOwner(BookingEntity booking){
      return booking.getRoom().getProperty().getUser().getId().equals(getLoggedUser().getId());
   }

   private Boolean isBookingUser(BookingEntity booking){
      return booking.getUser().getId().equals(getLoggedUser().getId());
   }

   private BookingEntity updateBooking(BookingEntity booking, BookingRequest request, RoomEntity room, BigDecimal totalPrice) {
      booking.setUser(getLoggedUser());
      booking.setStartDate(request.getStartDate());
      booking.setEndDate(request.getEndDate());
      booking.setGuestQuantity(request.getGuestQuantity());
      booking.setStatus(EBookingStatus.CONFIRMED);
      booking.setPaymentStatus(EPaymentStatus.PAID);
      booking.setType(EBookingType.GUEST);
      booking.setRoom(room);
      booking.setTotalPrice(totalPrice);
      return bookingRepository.save(booking);
   }

   private void validateOwnership(RoomEntity room){
      if (!room.getProperty().getUser().getId().equals(getLoggedUser().getId())) {
         throw new UnauthorizedException("You do not have permission to manage this booking");
      }
   }

   private void validateStatusForDeletion(BookingEntity booking){
      if(booking.getStatus().equals(EBookingStatus.CONFIRMED)){
         throw new BadRequestException("Cannot delete an active Booking");
      }
   }

   private BookingEntity saveBlock(BlockRequest request, RoomEntity room) {
      BookingEntity booking = BookingEntity.builder()
                                           .type(EBookingType.BLOCK)
                                           .user(getLoggedUser())
                                           .startDate(request.getStartDate())
                                           .endDate(request.getEndDate())
                                           .room(room)
                                           .status(EBookingStatus.BLOCKED)
                                           .build();
      return bookingRepository.save(booking);
   }

   private BookingEntity updateBlock(Long bookingId, BlockRequest request, RoomEntity room) {
      BookingEntity booking = getBooking(bookingId);
      booking.setUser(getLoggedUser());
      booking.setStartDate(request.getStartDate());
      booking.setEndDate(request.getEndDate());
      booking.setStatus(EBookingStatus.BLOCKED);
      booking.setType(EBookingType.BLOCK);
      booking.setRoom(room);
      return bookingRepository.save(booking);
   }

   private UserEntity getLoggedUser() {
      return UserContext.getInstance().getUser();
   }
}
