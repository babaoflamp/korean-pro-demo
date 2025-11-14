package com.mk.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mk.common.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * RuntimeException 발생 시 ApiResponse 타입의 응답 처리 handler
   * @param 처리할 exception
   * @return ApiResponse
   */
  @ExceptionHandler({ApiException.class, MemberInfoException.class})
  public ApiResponse<?> handleApiException(RuntimeException ex) {
    ApiResponse<?> apiResponse = null;

    if ( ex instanceof ApiException ) {
      ApiException apiEx = (ApiException) ex;
      apiResponse = apiEx.getApiResponse();

    } else if (ex instanceof MemberInfoException) {
      MemberInfoException memberInfoEx = (MemberInfoException) ex;
      apiResponse = memberInfoEx.getApiResponse();

    } else {
      apiResponse = ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

    return apiResponse;
  }

}
