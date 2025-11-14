package com.mk.config.validation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mk.common.ApiResponse;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class ValidationExceptionHandler {

  /**
   * "@Valid" 사용시 유효성검사 에러 처리 <br>
   * 발생한 에러 중 첫번째 에러에 대한 정보 return
   * 
   * @param e
   * @return ApiResponse(HttpStatus, message, Object) <br>
   *         HttpStatus : 400 <br>
   *         message : domain 객체 또는 DTO 에서 변수에 설정한 message <br>
   *         Object : RestController에서 파라미터로 받은 "@Valid" 선언 객체
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public static ApiResponse<Object> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {

    return new ApiResponse<Object>(HttpStatus.BAD_REQUEST,
        e.getBindingResult().getAllErrors().get(0).getDefaultMessage(), e.getTarget());
  }


  /**
   * "@Validated" 사용시 유효성검사 에러 처리<br>
   * 발생한 에러 중 첫번째 에러에 대한 정보 return<br>
   * Group을 지정하여 사용하는 경우, 제일 먼저 발생한 exception만 받음
   * 
   * @param e
   * @return ApiResponse(HttpStatus, message, Object) <br>
   *         HttpStatus : 400 <br>
   *         message : domain 객체 또는 DTO 에서 변수에 설정한 message <br>
   *         Object : 비지니스 계층에서 파라미터로 받은 "@Valid" 선언 객체
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public static ApiResponse<Object> handleConstraintViolationException(
      ConstraintViolationException e) {

    // ConstraintViolationException 첫번째 요소를 가져온다
    ConstraintViolation<?> exception = e.getConstraintViolations().stream().findFirst().get();

    return new ApiResponse<Object>(HttpStatus.BAD_REQUEST, exception.getMessage(),
        exception.getLeafBean());
  }


}
