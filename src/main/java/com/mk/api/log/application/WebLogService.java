package com.mk.api.log.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mk.api.log.application.dto.weblog.WebLogCreateDTO;
import com.mk.api.log.domain.WebLog;
import com.mk.api.log.infrastructure.WebLogRepository;
import com.mk.common.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WebLogService {

  private final static Logger LOGGER = LoggerFactory.getLogger(WebLogService.class);

  private final WebLogRepository webLogRepository;


  /**
   * 웹 로그 생성
   *
   * @param WebLogCreateDTO
   * @return ApiResponse
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public ApiResponse<Long> create(HttpServletRequest request, WebLogCreateDTO createDTO) {

    if (createDTO == null) {
      return ApiResponse.of(HttpStatus.BAD_REQUEST, null);
    }

    try {

      WebLog webLog = createDTO.toEntity();
      WebLog savedWebLog = webLogRepository.save(webLog);

      return savedWebLog != null ? ApiResponse.of(HttpStatus.CREATED, null)
          : ApiResponse.of(HttpStatus.NOT_FOUND, null);

    } catch (Exception e) {
      LOGGER.error(e.toString());
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
  }

}
