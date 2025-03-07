package com.foodtaste.service;

import com.foodtaste.dto.LoginRequestDTO;
import com.foodtaste.dto.LoginResponseDTO;

public interface AuthService {

	LoginResponseDTO verifyLogin(LoginRequestDTO loginRequestDto);
}
