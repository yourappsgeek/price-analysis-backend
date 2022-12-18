package com.purdue.priceanalysis.service.impl;

import com.purdue.priceanalysis.common.enums.Flag;
import com.purdue.priceanalysis.common.enums.ResponseCode;
import com.purdue.priceanalysis.common.exception.AppException;
import com.purdue.priceanalysis.common.util.Utils;
import com.purdue.priceanalysis.enums.RoleName;
import com.purdue.priceanalysis.model.entities.RoleEntity;
import com.purdue.priceanalysis.model.entities.UserEntity;
import com.purdue.priceanalysis.model.mapper.UserMapper;
import com.purdue.priceanalysis.model.payload.ApiResponse;
import com.purdue.priceanalysis.repository.UserRepository;
import com.purdue.priceanalysis.security.domain.UserPrincipal;
import com.purdue.priceanalysis.common.exception.ResourceNotFoundException;
import com.purdue.priceanalysis.service.IUserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService, IUserManagementService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException, AppException {

        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username : " + username));

        if(userEntity.getIsActive() == Integer.parseInt(Flag.INACTIVE.getFlag())) throw new AppException("Inactive user");

        return UserPrincipal.create(userEntity);
    }


    @Transactional
    public UserDetails loadUserById(Long id) {
        UserEntity user = userRepository.findByUserId(id).orElseThrow(
            () -> new ResourceNotFoundException("User", "id", id)
        );

        return UserPrincipal.create(user);
    }

    @Transactional
    public UserEntity getUserById(Long id) {
        UserEntity user = userRepository.findByUserId(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id)
        );

        return user;
    }

    @Override
    public ApiResponse<List<UserMapper>> getUserListByActiveStatus(Flag flag) {
        RoleEntity role = new RoleEntity();
        role.setRoleName(RoleName.USER);

        UserEntity searchParameter = new UserEntity();
        searchParameter.setIsActive(Integer.parseInt(flag.getFlag()));
        searchParameter.setRoles(Collections.singleton(role));

        List<UserMapper> userList = userRepository.findAll(Example.of(searchParameter))
                //userRepository.findAllByIsActiveAndRoles(Integer.parseInt(flag.getFlag()), Collections.singleton(role))
                .stream()
                .map(UserMapper::new)
                .collect(Collectors.toList());

        ResponseCode responseCode = !userList.isEmpty() ? ResponseCode.OPERATION_SUCCESSFUL : ResponseCode.NO_RECORD_FOUND;

        return new ApiResponse(responseCode.getCode(), responseCode.getMessage(), userList);


    }

    @Override
    public ApiResponse userActivation(Long userId, Flag flag) throws UsernameNotFoundException {
        if(Utils.isEmpty(userId)) return new ApiResponse(ResponseCode.INVALID_ARGUMENT.getCode(), "UserId cannot be empty");
        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with userId : " + userId));

        if(userEntity.getIsActive() == Integer.parseInt(flag.getFlag()))
            return new ApiResponse(ResponseCode.OPERATION_FAILED.getCode(), String.format("User is already %s", flag.name()));

        userEntity.setIsActive(Integer.parseInt(flag.getFlag()));

        userRepository.save(userEntity);

        return new ApiResponse(ResponseCode.OPERATION_SUCCESSFUL.getCode(), ResponseCode.OPERATION_SUCCESSFUL.getMessage());
    }
}