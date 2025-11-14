package com.mk.config.exception;

import com.mk.common.ApiResponse;

import lombok.Getter;

@Getter
public class MemberInfoException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private final ApiResponse<?> apiResponse;

  public MemberInfoException(ApiResponse<?> apiResponse) {
    super(apiResponse.getMessage());
    this.apiResponse = apiResponse;
  }

}
