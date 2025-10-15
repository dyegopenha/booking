package com.example.booking.configuration;

import com.example.booking.entity.UserEntity;

import lombok.Getter;

public class UserContext {

   @Getter
   private static UserContext instance = new UserContext();

   ThreadLocal<UserEntity> globalUser = new ThreadLocal<>();

   private UserContext() {}

   public UserEntity getUser() {
      return globalUser.get();
   }

   public void setUser(UserEntity user) {
      globalUser.set(user);
   }
}
