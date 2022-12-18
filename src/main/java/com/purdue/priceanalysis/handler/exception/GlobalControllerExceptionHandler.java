package com.purdue.priceanalysis.handler.exception;

import com.purdue.priceanalysis.common.exception.ResourceNotFoundException;
import com.purdue.priceanalysis.model.payload.ApiResponse;
import com.purdue.priceanalysis.common.enums.ResponseCode;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(value={RuntimeException.class})
    @ResponseStatus(value = HttpStatus.OK)
    ApiResponse exceptionHandlingResponse(Exception exception) {
        return new ApiResponse(ResponseCode.RUNTIME_ERROR.getCode(), ResponseCode.RUNTIME_ERROR.getMessage());
    }

    @ExceptionHandler(value={BadCredentialsException.class})
    @ResponseStatus(value = HttpStatus.OK)
    ApiResponse badCredentialsHandlingResponse(Exception exception) {
        return new ApiResponse(ResponseCode.BAD_CREDENTIALS.getCode(), ResponseCode.AUTHENTICATION_FAIL.getMessage());
    }

    @ExceptionHandler(value={AccessDeniedException.class})
    @ResponseStatus(value = HttpStatus.OK)
    ApiResponse accessDeniedExceptionHandlingResponse(Exception exception) {
        return new ApiResponse(ResponseCode.UNAUTHORIZED_ACCESS.getCode(), ResponseCode.UNAUTHORIZED_ACCESS.getMessage());
    }

    @ExceptionHandler(value={InternalAuthenticationServiceException.class, UsernameNotFoundException.class, ResourceNotFoundException.class})
    @ResponseStatus(value = HttpStatus.OK)
    ApiResponse exceptionMessageHandlingResponse(Exception exception) {
        return new ApiResponse(ResponseCode.AUTHENTICATION_FAIL.getCode(), exception.getMessage());
    }

    @ExceptionHandler(value={MethodArgumentNotValidException.class})
    @ResponseStatus(value = HttpStatus.OK)
    ApiResponse methodArgumentNotValidExceptionMessageHandlingResponse(MethodArgumentNotValidException exception) {
        return new ApiResponse(ResponseCode.VALIDATION_ERROR.getCode(), ResponseCode.VALIDATION_ERROR.getMessage(), exception.getAllErrors());
    }
}
