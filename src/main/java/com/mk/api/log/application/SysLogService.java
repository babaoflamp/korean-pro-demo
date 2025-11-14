package com.mk.api.log.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mk.api.log.application.dto.syslog.SysLogCreateDTO;
import com.mk.api.log.domain.SysLog;
import com.mk.api.log.infrastructure.SysLogRepository;
import com.mk.common.ApiResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SysLogService {

  private static final Logger LOGGER = LoggerFactory.getLogger(SysLogService.class);

  private final SysLogRepository sysLogRepository;

  /**
   * 시스템 로그 생성
   *
   * @param SysLogCreateDTO
   * @return ApiResponse
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public ApiResponse<Long> create(SysLogCreateDTO createDTO) {
    if( createDTO == null ) {
      return ApiResponse.of(HttpStatus.BAD_REQUEST, null);
    }

    try {

      SysLog sysLog = createDTO.toEntity();
      SysLog savedSysLogresult = sysLogRepository.save(sysLog);

      return savedSysLogresult != null ? ApiResponse.of(HttpStatus.CREATED, null)
          : ApiResponse.of(HttpStatus.NOT_FOUND, null);

    } catch (Exception e) {
      LOGGER.error(e.getMessage());
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
  }

}
