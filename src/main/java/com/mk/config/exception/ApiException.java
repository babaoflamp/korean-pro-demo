package com.mk.config.exception;

import org.springframework.http.HttpStatus;
import com.mk.common.ApiResponse;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private final ApiResponse<?> apiResponse;

  public ApiException(ApiResponse<?> apiResponse) {
    super(apiResponse.getMessage());
    this.apiResponse = apiResponse;
  }

  public HttpStatus getStatus() {
      return HttpStatus.valueOf(apiResponse.getStatus());  // ApiResponse의 상태 코드 반환
  }

}
