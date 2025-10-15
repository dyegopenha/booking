package com.example.booking.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.booking.entity.BookingEntity;
import com.example.booking.enums.EBookingStatus;
import com.example.booking.enums.EPropertyType;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

   @Query("SELECT b FROM BookingEntity b WHERE b.room.id = :roomId " +
         "AND (b.status = 'CONFIRMED' OR b.status = 'BLOCKED') " +
         "AND (b.startDate < :endDate " +
         "AND b.endDate > :startDate)")
   Optional<List<BookingEntity>> findActiveBookingsForDates(Long roomId, LocalDate startDate, LocalDate endDate);

   @Query("SELECT b FROM BookingEntity b WHERE b.room.id = :roomId " +
         "AND (b.status = 'CONFIRMED' OR b.status = 'BLOCKED') " +
         "AND (b.startDate < :endDate " +
         "AND b.endDate > :startDate) " +
         "AND b.id != :updatedBookingId")
   Optional<List<BookingEntity>> findActiveBookingsForDatesExcludingCurrent(Long roomId, LocalDate startDate, LocalDate endDate, Long updatedBookingId);

   @Query("SELECT b FROM BookingEntity b " +
         "WHERE (:userId IS NULL OR b.user.id = :userId OR b.room.property.user.id = :userId) " +
         "AND (:propertyName IS NULL OR b.room.property.name LIKE CONCAT('%', :propertyName, '%')) " +
         "AND (:roomName IS NULL OR b.room.name LIKE CONCAT('%', :roomName, '%')) " +
         "AND (:propertyType IS NULL OR b.room.property.type = :propertyType) " +
         "AND (:bookingStatus IS NULL OR b.status = :bookingStatus)")
   Page<BookingEntity> findBookings(
         @Param("userId") Long userId,
         @Param("propertyName") String propertyName,
         @Param("roomName") String roomName,
         @Param("propertyType") EPropertyType propertyType,
         @Param("bookingStatus") EBookingStatus bookingStatus,
         Pageable pageable
   );
}
