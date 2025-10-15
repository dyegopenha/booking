package com.example.booking.entity;

import java.util.List;

import com.example.booking.enums.EPropertyType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "properties")
public class PropertyEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(nullable = false)
   private String name;

   @Enumerated(EnumType.STRING)
   @Column(nullable = false)
   private EPropertyType type;

   @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
   private List<RoomEntity> rooms;

   @ManyToOne
   @JoinColumn(name = "user_id", nullable = false)
   private UserEntity user;
}
