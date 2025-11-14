package com.mk.common;

import org.springframework.http.HttpStatus;

public class ApiResponse<T> {

  // RestAPI Response를 위한 공통 처리

  private int status;
  private String message;
  private T data;

  public ApiResponse(HttpStatus status, String message, T data) {
    this.status = status.value();
    this.message = message;
    this.data = data;
  }

  public static <T> ApiResponse<T> of(HttpStatus status, T data) {
    String message = getDefaultMessageForStatusCode(status);
    return new ApiResponse<>(status, message, data);
  }

  private static String getDefaultMessageForStatusCode(HttpStatus status) {

    switch (status) {
      case OK: // 200
        return "Operation succeeded";
      case CREATED: // 201
        return "Resource created successfully";
      case NO_CONTENT: // 204
        return "Resource deleted successfully";
      case BAD_REQUEST: // 400
        return "Invalid request format";
      case UNAUTHORIZED: // 401
        return "Authentication required";
      case FORBIDDEN: // 403
        return "Access denied";
      case NOT_FOUND: // 404
        return "Resource not found";
      case CONFLICT: // 409
        return "Conflict detected";
      case UNPROCESSABLE_ENTITY: // 422
        return "Unable to process the contained instructions";
      case INTERNAL_SERVER_ERROR: // 500
        return "Internal server error occurred";
      case REQUEST_TIMEOUT: // 408
        return "Authentication time expired";
      default:
        return "HTTP Status " + status.value();
    }
  }

  public int getStatus() {
    return status;
  }

  public String getMessage() {
    return message;
  }

  public T getData() {
    return data;
  }
}
