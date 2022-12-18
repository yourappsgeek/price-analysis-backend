package com.purdue.priceanalysis.service;

import com.purdue.priceanalysis.common.enums.Flag;
import com.purdue.priceanalysis.model.mapper.UserMapper;
import com.purdue.priceanalysis.model.payload.ApiResponse;

import java.util.List;

public interface IUserManagementService {

    ApiResponse<List<UserMapper>> getUserListByActiveStatus(Flag flag);
    ApiResponse userActivation(Long userId, Flag flag);

}
