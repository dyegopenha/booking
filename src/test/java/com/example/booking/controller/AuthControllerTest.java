package com.example.booking.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.example.booking.dto.request.AuthRequest;
import com.example.booking.factory.AuthRequestFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class AuthControllerTest {

   @Autowired
   private MockMvc mockMvc;

   @Autowired
   private ObjectMapper objectMapper;

   @Test
   public void whenLoginThenReturnOkStatus() throws Exception {
      AuthRequest validAuthRequest = AuthRequestFactory.buildValidAuthRequest();
      String jsonRequest = objectMapper.writeValueAsString(validAuthRequest);

      mockMvc.perform(post("/api/v1/auth/login", validAuthRequest)
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(jsonRequest))
             .andExpect(status().isOk())
             .andExpect(jsonPath("$.token").exists());
   }

   @Test
   public void whenLoginThenReturnUnauthorized() throws Exception {
      AuthRequest invalidAuthRequest = AuthRequestFactory.buildInvalidAuthRequest();
      String jsonRequest = objectMapper.writeValueAsString(invalidAuthRequest);

      mockMvc.perform(post("/api/v1/auth/login", invalidAuthRequest)
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(jsonRequest))
             .andExpect(status().isUnauthorized())
             .andExpect(jsonPath("$.token").doesNotExist());
   }
}
