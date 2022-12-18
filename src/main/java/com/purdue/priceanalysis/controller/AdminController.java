package com.purdue.priceanalysis.controller;

import com.purdue.priceanalysis.common.enums.Flag;
import com.purdue.priceanalysis.model.payload.ApiResponse;
import com.purdue.priceanalysis.service.IUserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private IUserManagementService userManagementService;

    @GetMapping("/activeUserList")
    public ApiResponse getAllActiveUserList() {
        return userManagementService.getUserListByActiveStatus(Flag.ACTIVE);
    }

    @GetMapping("/inactiveUserList")
    public ApiResponse getAllInactiveUserList() {
        return userManagementService.getUserListByActiveStatus(Flag.INACTIVE);
    }

    @GetMapping("/activateUser/{id}")
    public ApiResponse activateUser(@PathVariable Long id) {
        return userManagementService.userActivation(id, Flag.ACTIVE);
    }

    @GetMapping("/deactivateUser/{id}")
    public ApiResponse deactivateUser(@PathVariable Long id) {
        return userManagementService.userActivation(id, Flag.INACTIVE);
    }
}
