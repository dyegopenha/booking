package com.example.booking.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.booking.entity.RoomEntity;
import com.example.booking.enums.EPropertyType;

import jakarta.persistence.LockModeType;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long> {

   @Override
   @Lock(LockModeType.PESSIMISTIC_WRITE)
   Optional<RoomEntity> findById(Long id);

   @Query("""
        SELECT r FROM RoomEntity r JOIN r.property p
        WHERE (:propertyName IS NULL OR p.name LIKE CONCAT('%', :propertyName, '%'))
        AND (:roomName IS NULL OR r.name LIKE CONCAT('%', :roomName, '%'))
        AND (:propertyType IS NULL OR p.type = :propertyType)
        AND (:guestQuantity IS NULL OR r.capacity >= :guestQuantity)
        AND NOT EXISTS (
            SELECT b FROM BookingEntity b 
            WHERE b.room = r 
            AND (b.status = 'CONFIRMED' OR b.status = 'BLOCKED')
            AND (b.startDate < :endDate AND b.endDate > :startDate)
        )
    """)
   Page<RoomEntity> findAvailableRooms(
         String propertyName, String roomName, EPropertyType propertyType, Integer guestQuantity,
         LocalDate startDate, LocalDate endDate, Pageable pageable);
}
