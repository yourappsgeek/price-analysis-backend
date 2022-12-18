package com.purdue.priceanalysis.service.impl;

import com.purdue.priceanalysis.enums.RoleName;
import com.purdue.priceanalysis.model.entities.RoleEntity;
import com.purdue.priceanalysis.model.entities.UserEntity;
import com.purdue.priceanalysis.model.payload.ApiResponse;
import com.purdue.priceanalysis.model.payload.auth.TokenRequest;
import com.purdue.priceanalysis.model.payload.auth.TokenResponse;
import com.purdue.priceanalysis.model.payload.registration.RegistrationRequest;
import com.purdue.priceanalysis.repository.RoleRepository;
import com.purdue.priceanalysis.repository.UserRepository;
import com.purdue.priceanalysis.security.JwtTokenProvider;
import com.purdue.priceanalysis.service.IAuthenticationService;
import com.purdue.priceanalysis.common.enums.Flag;
import com.purdue.priceanalysis.common.enums.ResponseCode;
import com.purdue.priceanalysis.common.exception.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AuthenticationServiceImpl implements IAuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Override
    public TokenResponse generateToken(TokenRequest tokenRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        tokenRequest.getUsername(),
                        tokenRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);

        return new TokenResponse(jwt);
    }

    @Override
    public ApiResponse registerUser(RegistrationRequest registrationRequest, RoleName roleName) {
        if(userRepository.existsByUsername(registrationRequest.getUsername())) {
            return new ApiResponse (ResponseCode.USERNAME_ALREADY_EXISTS.getCode(), ResponseCode.USERNAME_ALREADY_EXISTS.getMessage());
        }

        RoleEntity role = roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new AppException(String.format("%s Role not set", roleName.name())));

        UserEntity userEntity = new UserEntity(registrationRequest,
                passwordEncoder.encode(registrationRequest.getPassword()),
                roleName == RoleName.ADMIN ? Integer.parseInt(Flag.ACTIVE.getFlag()) : Integer.parseInt(Flag.INACTIVE.getFlag()),
                LocalDateTime.now(),
                Collections.singleton(role));

        userRepository.save(userEntity);

        return new ApiResponse(ResponseCode.OPERATION_SUCCESSFUL.getCode(), "User registered successfully");
    }

    @Override
    public ApiResponse logout(HttpServletRequest request) {
        String token = JwtTokenProvider.getJwtFromRequest(request);
        JwtTokenProvider.addBlackList(token);


        SecurityContextHolder.clearContext();

        return new ApiResponse(ResponseCode.OPERATION_SUCCESSFUL.getCode(), "Logout successfully");
    }
}
