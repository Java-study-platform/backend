package com.study.user.Service;

import com.study.user.DTO.TokenResponse;
import com.study.user.DTO.UserLoginModel;
import com.study.user.DTO.UserRegistrationModel;

public interface AuthService {
    void registerUser(UserRegistrationModel userRegistrationModel);

    TokenResponse loginUser(UserLoginModel userLoginModel);

    void logoutUser(String userId);
}
