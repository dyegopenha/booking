package com.example.booking.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.example.booking.dto.request.AuthRequest;
import com.example.booking.dto.request.BlockRequest;
import com.example.booking.dto.request.BookingRequest;
import com.example.booking.factory.AuthRequestFactory;
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

   private String jwtToken;

   void setUpAuth(AuthRequest authRequest) throws Exception {
      String jsonRequest = objectMapper.writeValueAsString(authRequest);

      MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(jsonRequest))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.token").exists())
                                .andReturn();

      jwtToken = objectMapper.readTree(result.getResponse().getContentAsString()).get("token").asText();
   }

   @Test
   public void whenCreateBookingThenReturnCreatedStatus() throws Exception {
      AuthRequest validAuthRequest = AuthRequestFactory.buildValidAuthRequest();
      setUpAuth(validAuthRequest);

      BookingRequest validBookingRequest = BookingFactory.buildBookingRequest();
      String jsonRequest = objectMapper.writeValueAsString(validBookingRequest);

      mockMvc.perform(post("/api/v1/bookings")
                   .header("Authorization", "Bearer " + jwtToken)
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(jsonRequest))
             .andExpect(status().isCreated())
             .andExpect(jsonPath("$.id").exists());
   }

   @Test
   public void whenRebookCanceledBookingThenReturnOkStatus() throws Exception {
      AuthRequest validAuthRequest = AuthRequestFactory.buildValidAuthRequest();
      setUpAuth(validAuthRequest);

      mockMvc.perform(put("/api/v1/bookings/2/rebook")
                   .header("Authorization", "Bearer " + jwtToken)
                   .contentType(MediaType.APPLICATION_JSON))
             .andExpect(status().isOk())
             .andExpect(jsonPath("$.id").exists());
   }

   @Test
   public void whenCancelBookingThenReturnOkStatus() throws Exception {
      AuthRequest validAuthRequest = AuthRequestFactory.buildValidAuthRequest();
      setUpAuth(validAuthRequest);

      mockMvc.perform(put("/api/v1/bookings/4/cancel")
                   .header("Authorization", "Bearer " + jwtToken)
                   .contentType(MediaType.APPLICATION_JSON))
             .andExpect(status().isOk())
             .andExpect(jsonPath("$.id").exists());
   }

   @Test
   public void whenGetBookingByIdThenReturnOkStatus() throws Exception {
      AuthRequest validAuthRequest = AuthRequestFactory.buildValidAuthRequest();
      setUpAuth(validAuthRequest);

      mockMvc.perform(get("/api/v1/bookings/4")
                   .header("Authorization", "Bearer " + jwtToken)
                   .contentType(MediaType.APPLICATION_JSON))
             .andExpect(status().isOk())
             .andExpect(jsonPath("$.id").exists());
   }

   @Test
   public void whenGetAllBookingsThenReturnOkStatus() throws Exception {
      AuthRequest validAuthRequest = AuthRequestFactory.buildValidAuthRequest();
      setUpAuth(validAuthRequest);

      mockMvc.perform(get("/api/v1/bookings")
                   .param("page", "0")
                   .param("size", "10")
                   .header("Authorization", "Bearer " + jwtToken)
                   .contentType(MediaType.APPLICATION_JSON))
             .andExpect(status().isOk())
             .andExpect(jsonPath("$.content").exists())
             .andExpect(jsonPath("$.content[0].id").exists());
   }

   @Test
   public void whenCreateBlockThenReturnCreatedStatus() throws Exception {
      AuthRequest propertyOwnerAuthRequest = AuthRequestFactory.buildValidPropertyOwnerAuthRequest();
      setUpAuth(propertyOwnerAuthRequest);

      BlockRequest validBlockRequest = BookingFactory.buildValidBlockRequest();
      String jsonRequest = objectMapper.writeValueAsString(validBlockRequest);

      mockMvc.perform(post("/api/v1/bookings/blocks")
                   .header("Authorization", "Bearer " + jwtToken)
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(jsonRequest))
             .andExpect(status().isCreated())
             .andExpect(jsonPath("$.id").exists());
   }

   @Test
   public void whenUpdateBlockThenReturnOkStatus() throws Exception {
      AuthRequest propertyOwnerAuthRequest = AuthRequestFactory.buildValidPropertyOwnerAuthRequest();
      setUpAuth(propertyOwnerAuthRequest);

      BlockRequest validUpdateBlockRequest = BookingFactory.buildValidUpdateBlockRequest();
      String jsonRequest = objectMapper.writeValueAsString(validUpdateBlockRequest);

      mockMvc.perform(put("/api/v1/bookings/blocks/1")
                   .header("Authorization", "Bearer " + jwtToken)
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(jsonRequest))
             .andExpect(status().isOk())
             .andExpect(jsonPath("$.id").exists());
   }

   @Test
   public void whenDeleteBlockThenReturnNoContent() throws Exception {
      AuthRequest propertyOwnerAuthRequest = AuthRequestFactory.buildValidPropertyOwnerAuthRequest();
      setUpAuth(propertyOwnerAuthRequest);

      mockMvc.perform(delete("/api/v1/bookings/3")
                   .header("Authorization", "Bearer " + jwtToken)
                   .contentType(MediaType.APPLICATION_JSON))
             .andExpect(status().isNoContent());
   }
}
