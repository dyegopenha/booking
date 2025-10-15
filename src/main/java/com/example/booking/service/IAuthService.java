package com.example.booking.service;

import com.example.booking.dto.response.AuthResponse;

public interface IAuthService {

   AuthResponse login(String username, String password);
}
