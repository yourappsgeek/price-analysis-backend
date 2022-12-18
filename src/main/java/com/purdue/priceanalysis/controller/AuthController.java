package com.purdue.priceanalysis.controller;

import com.purdue.priceanalysis.enums.RoleName;
import com.purdue.priceanalysis.model.payload.ApiResponse;
import com.purdue.priceanalysis.model.payload.auth.TokenRequest;
import com.purdue.priceanalysis.model.payload.auth.TokenResponse;
import com.purdue.priceanalysis.model.payload.registration.RegistrationRequest;
import com.purdue.priceanalysis.service.IAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private IAuthenticationService authenticationService;

    @PostMapping("/tokenRequest")
    public TokenResponse tokenRequest(@Valid @RequestBody TokenRequest tokenRequest) {
        return authenticationService.generateToken(tokenRequest);
    }

    @GetMapping("/logout")
    public ApiResponse invalidateToken(HttpServletRequest request) {
        return authenticationService.logout(request);
    }

    @PostMapping("/userRegistration")
    public ApiResponse userRegistration(@Valid @RequestBody RegistrationRequest registrationRequest) {
        return authenticationService.registerUser(registrationRequest, RoleName.USER);
    }

    @PostMapping("/adminRegistration")
    public ApiResponse adminRegistration(@Valid @RequestBody RegistrationRequest registrationRequest) {
        return authenticationService.registerUser(registrationRequest, RoleName.ADMIN);
    }
}
