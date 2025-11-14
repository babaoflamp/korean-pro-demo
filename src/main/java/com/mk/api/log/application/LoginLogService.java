package com.mk.api.log.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mk.api.log.application.dto.loginlog.LoginLogCreateDTO;
import com.mk.api.log.domain.LoginLog;
import com.mk.api.log.infrastructure.LoginLogRepository;
import com.mk.common.ApiResponse;
import com.mk.common.ClientInfoUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginLogService {

  private static final Logger LOGGER = LoggerFactory.getLogger(LoginLogService.class);
  
  private final LoginLogRepository loginLogRepository;

  /**
   * 로그인 로그 생성
   * @param HttpServletRequest
   * @param LoginLogCreateDTO
   * @return ApiResponse
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public ApiResponse<Long> create(HttpServletRequest request, LoginLogCreateDTO createDTO) {
    if (createDTO == null ) {
      return ApiResponse.of(HttpStatus.BAD_REQUEST, null);
    }

    try {
      LoginLogCreateDTO dto = createDTO
          .toBuilder()
          .conectIp(ClientInfoUtil.getIp(request))
          .conectOs(ClientInfoUtil.getOs(request))
          .conectBrowser(ClientInfoUtil.getBrowser(request))
          .build();

      LoginLog loginLog = dto.toEntity();
      LoginLog savedLoginLog = loginLogRepository.save(loginLog);

      return savedLoginLog != null ? ApiResponse.of(HttpStatus.CREATED, null)
          : ApiResponse.of(HttpStatus.NOT_FOUND, null);

    } catch (Exception e) {
      LOGGER.error(e.getMessage());
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
  }

}
