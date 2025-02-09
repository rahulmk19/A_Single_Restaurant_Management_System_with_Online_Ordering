package com.foodtaste.service;

import com.foodtaste.dto.UserRequest;

public interface PublicAuthService {

	String register(UserRequest request);

	String login(UserRequest request);
}
