package com.foodtaste.service;

import com.foodtaste.dto.LoginRequest;
import com.foodtaste.dto.UserRequest;

public interface PublicAuthService {

	UserRequest register(UserRequest request);

	String login(LoginRequest request);
}
