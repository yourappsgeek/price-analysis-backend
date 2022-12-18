package com.purdue.priceanalysis.service;

import com.purdue.priceanalysis.enums.RoleName;
import com.purdue.priceanalysis.model.mapper.UserMapper;
import com.purdue.priceanalysis.model.payload.ApiResponse;
import com.purdue.priceanalysis.model.payload.auth.TokenRequest;
import com.purdue.priceanalysis.model.payload.auth.TokenResponse;
import com.purdue.priceanalysis.model.payload.registration.RegistrationRequest;

import javax.servlet.http.HttpServletRequest;

public interface IAuthenticationService {

    TokenResponse generateToken(TokenRequest tokenRequest);
    ApiResponse<UserMapper> registerUser(RegistrationRequest registrationRequest, RoleName roleName);
    ApiResponse logout(HttpServletRequest request);
}
