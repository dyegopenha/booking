package com.example.booking.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

   @Test
   public void whenGetAvailableRoomsThenReturnOkStatus() throws Exception {
      mockMvc.perform(get("/api/v1/rooms")
                   .param("startDate", LocalDate.now().plusDays(1).toString())
                   .param("endDate", LocalDate.now().plusDays(3).toString())
                   .param("page", "0")
                   .param("size", "10")
                   .header("User-Id", "1")
                   .contentType(MediaType.APPLICATION_JSON))
             .andExpect(status().isOk())
             .andExpect(jsonPath("$.content").exists())
             .andExpect(jsonPath("$.content[0].id").exists());
   }
}
