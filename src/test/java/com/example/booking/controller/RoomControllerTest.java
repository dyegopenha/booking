package com.example.booking.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.example.booking.dto.request.AuthRequest;
import com.example.booking.factory.AuthRequestFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class RoomControllerTest {

   @Autowired
   private MockMvc mockMvc;

   @Autowired
   private ObjectMapper objectMapper;

   private String jwtToken;

   @BeforeEach
   void setUp() throws Exception {
      AuthRequest validAuthRequest = AuthRequestFactory.buildValidAuthRequest();
      String jsonRequest = objectMapper.writeValueAsString(validAuthRequest);

      MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(jsonRequest))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.token").exists())
                                .andReturn();

      jwtToken = objectMapper.readTree(result.getResponse().getContentAsString()).get("token").asText();
   }

   @Test
   public void whenGetAvailableRoomsThenReturnOkStatus() throws Exception {
      mockMvc.perform(get("/api/v1/rooms")
                   .param("startDate", LocalDate.now().plusDays(1).toString())
                   .param("endDate", LocalDate.now().plusDays(3).toString())
                   .param("page", "0")
                   .param("size", "10")
                   .header("Authorization", "Bearer " + jwtToken)
                   .contentType(MediaType.APPLICATION_JSON))
             .andExpect(status().isOk())
             .andExpect(jsonPath("$.content").exists())
             .andExpect(jsonPath("$.content[0].id").exists());
   }
}
