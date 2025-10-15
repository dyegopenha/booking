package com.example.booking.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.example.booking.dto.request.BlockRequest;
import com.example.booking.dto.request.BookingRequest;
import com.example.booking.factory.BookingFactory;
import com.example.booking.repository.BookingRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class BookingControllerTest {

   @Autowired
   private MockMvc mockMvc;

   @Autowired
   private ObjectMapper objectMapper;

   @Autowired
   private BookingRepository bookingRepository;

   @BeforeEach
   void setUp() {

   }

   @Test
   public void whenCreateBookingThenReturnCreatedStatus() throws Exception {
      BookingRequest validBookingRequest = BookingFactory.buildBookingRequest();
      String jsonRequest = objectMapper.writeValueAsString(validBookingRequest);

      mockMvc.perform(post("/api/v1/bookings")
                   .header("User-Id", "1")
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(jsonRequest))
             .andExpect(status().isCreated())
             .andExpect(jsonPath("$.id").exists());
   }

   @Test
   public void whenRebookCanceledBookingThenReturnOkStatus() throws Exception {
      mockMvc.perform(put("/api/v1/bookings/2/rebook")
                   .header("User-Id", "1")
                   .contentType(MediaType.APPLICATION_JSON))
             .andExpect(status().isOk())
             .andExpect(jsonPath("$.id").exists());
   }

   @Test
   public void whenCancelBookingThenReturnOkStatus() throws Exception {
      mockMvc.perform(put("/api/v1/bookings/4/cancel")
                   .header("User-Id", "1")
                   .contentType(MediaType.APPLICATION_JSON))
             .andExpect(status().isOk())
             .andExpect(jsonPath("$.id").exists());
   }

   @Test
   public void whenGetBookingByIdThenReturnOkStatus() throws Exception {
      mockMvc.perform(get("/api/v1/bookings/4")
                   .header("User-Id", "1")
                   .contentType(MediaType.APPLICATION_JSON))
             .andExpect(status().isOk())
             .andExpect(jsonPath("$.id").exists());
   }

   @Test
   public void whenGetAllBookingsThenReturnOkStatus() throws Exception {
      mockMvc.perform(get("/api/v1/bookings")
                   .param("page", "0")
                   .param("size", "10")
                   .header("User-Id", "1")
                   .contentType(MediaType.APPLICATION_JSON))
             .andExpect(status().isOk())
             .andExpect(jsonPath("$.content").exists())
             .andExpect(jsonPath("$.content[0].id").exists());
   }

   @Test
   public void whenCreateBlockThenReturnCreatedStatus() throws Exception {
      BlockRequest validBlockRequest = BookingFactory.buildValidBlockRequest();
      String jsonRequest = objectMapper.writeValueAsString(validBlockRequest);

      mockMvc.perform(post("/api/v1/bookings/blocks")
                   .header("User-Id", "2")
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(jsonRequest))
             .andExpect(status().isCreated())
             .andExpect(jsonPath("$.id").exists());
   }

   @Test
   public void whenUpdateBlockThenReturnOkStatus() throws Exception {
      BlockRequest validUpdateBlockRequest = BookingFactory.buildValidUpdateBlockRequest();
      String jsonRequest = objectMapper.writeValueAsString(validUpdateBlockRequest);

      mockMvc.perform(put("/api/v1/bookings/blocks/1")
                   .header("User-Id", "2")
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(jsonRequest))
             .andExpect(status().isOk())
             .andExpect(jsonPath("$.id").exists());
   }

   @Test
   public void whenDeleteBlockThenReturnNoContent() throws Exception {
      mockMvc.perform(delete("/api/v1/bookings/3")
                   .header("User-Id", "2")
                   .contentType(MediaType.APPLICATION_JSON))
             .andExpect(status().isNoContent());
   }
}
