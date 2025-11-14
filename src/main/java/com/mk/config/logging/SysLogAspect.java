package com.mk.config.logging;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mk.api.log.application.SysLogService;
import com.mk.api.log.application.dto.syslog.SysLogCreateDTO;
import com.mk.api.log.value.ErrorStatus;
import com.mk.api.log.value.ProcessSection;
import com.mk.common.ApiResponse;
import com.mk.common.ClientInfoUtil;
import com.mk.config.exception.ApiException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Component
@Aspect
@RequiredArgsConstructor
public class SysLogAspect {

  private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

  private final ObjectMapper objectMapper;
  private final SysLogService sysLogService;

  /**
   * selectLog
   */
  @Pointcut("execution( * com.mk.api..*Service..find*(..)) "
      + "&& !execution( * com.mk.api..*Service..findAllMenuBySysId(..)) ")
  private void selectLog() {};

  /**
   * createLog
   */
  @Pointcut("execution( * com.mk.api..*Service..create*(..)) "
      + "&& !execution( * com.mk.api..WebLogService.create*(..)) "
      + "&& !execution( * com.mk.api..SysLogService.create*(..)) "
      + "&& !execution( * com.mk.api..LoginLogService.create*(..)) ")
  private void createLog() {};

  /**
   * updateLog
   */
  @Pointcut("execution( * com.mk.api..*Service..update*(..))")
  private void updateLog() {};

  /**
   * deleteLog
   */
  @Pointcut("execution( * com.mk.api..*Service..delete*(..))")
  private void deleteLog() {};

  @Pointcut("!@annotation(com.mk.config.logging.NoLogging)")
  private void noLogging() {};

  // SELECT
  @Around("selectLog() && noLogging()")
  public Object selectLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    Object result = null;
    boolean exceptionThrown = false;

    try {
      StopWatchContext.start();
      result = proceedingJoinPoint.proceed();
      return result;
    } catch (Exception e) {
      exceptionThrown = true;
      LOGGER.error("selectLog exception : " + e.toString());
      throw e;
    } finally {
      StopWatchContext.stop();
      if (!exceptionThrown) {
        createSysLog(result, proceedingJoinPoint, StopWatchContext.getTime(), ProcessSection.R);
      }
    }
  }

  @AfterThrowing(pointcut = "selectLog()", throwing = "ex")
  public void selectAfterThrowing(JoinPoint joinPoint, Throwable ex) throws Throwable {
    createErrorLog(ex, joinPoint, StopWatchContext.getTime(), ProcessSection.R);
  }

  // CREATE
  @Around("createLog() && noLogging()")
  public Object createLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    Object result = null;
    boolean exceptionThrown = false;

    try {
      StopWatchContext.start();
      result = proceedingJoinPoint.proceed();
      return result;
    } catch (Exception e) {
      exceptionThrown = true;
      LOGGER.error("createLog exception : " + e.toString());
      throw e;
    } finally {
      StopWatchContext.stop();
      if (!exceptionThrown) {
        createSysLog(result, proceedingJoinPoint, StopWatchContext.getTime(), ProcessSection.C);
      }
    }
  }

  @AfterThrowing(pointcut = "createLog()", throwing = "ex")
  public void createAfterThrowing(JoinPoint joinPoint, Throwable ex) throws Throwable {
    createErrorLog(ex, joinPoint, StopWatchContext.getTime(), ProcessSection.C);
  }

  // UPDATE
  @Around("updateLog() && noLogging()")
  public Object updateLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    Object result = null;
    boolean exceptionThrown = false;

    try {
      StopWatchContext.start();
      result = proceedingJoinPoint.proceed();
      return result;
    } catch (Exception e) {
      exceptionThrown = true;
      LOGGER.error("updateLog exception : " + e.toString());
      throw e;
    } finally {
      StopWatchContext.stop();
      if (!exceptionThrown) {
        createSysLog(result, proceedingJoinPoint, StopWatchContext.getTime(), ProcessSection.U);
      }
    }
  }

  @AfterThrowing(pointcut = "updateLog()", throwing = "ex")
  public void updateAfterThrowing(JoinPoint joinPoint, Throwable ex) throws Throwable {
    createErrorLog(ex, joinPoint, StopWatchContext.getTime(), ProcessSection.U);
  }

  // DELETE
  @Around("deleteLog() && noLogging()")
  public Object deleteLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    Object result = null;
    boolean exceptionThrown = false;

    try {
      StopWatchContext.start();
      result = proceedingJoinPoint.proceed();
      return result;
    } catch (Exception e) {
      exceptionThrown = true;
      LOGGER.error("deleteLog exception : " + e.toString());
      throw e;
    } finally {
      StopWatchContext.stop();
      if (!exceptionThrown) {
        createSysLog(result, proceedingJoinPoint, StopWatchContext.getTime(), ProcessSection.D);
      }
    }
  }

  @AfterThrowing(pointcut = "deleteLog()", throwing = "ex")
  public void deleteAfterThrowing(JoinPoint joinPoint, Throwable ex) throws Throwable {
    createErrorLog(ex, joinPoint, StopWatchContext.getTime(), ProcessSection.D);
  }

  /**
   * sysLog 생성 (정상 처리 시)
   */
  private void createSysLog(Object result, ProceedingJoinPoint proceedingJoinPoint, double time,
      ProcessSection processSec) {

    try {
      HttpServletRequest request =
          ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
      HttpSession session = request.getSession();
      String requestURI = request.getRequestURI();

      ErrorStatus errorYn = ErrorStatus.N;
      int errorStat = 0;
      if (result instanceof ApiResponse) {
        ApiResponse<?> apiResponse = resultToResponse(result);
        LOGGER.info("create syslog > apiResponse.status : {}", apiResponse.getStatus());

        if (!HttpStatus.valueOf(apiResponse.getStatus()).is2xxSuccessful()) {
          errorYn = ErrorStatus.Y;
          errorStat = apiResponse.getStatus();
        }
      }

      LOGGER
          .info("create syslog > uri : {} | processSec : {}", request.getRequestURI(), processSec);

      // 특정 URI는 로그 생성 제외
      if (!"/api/logout".equals(requestURI) && !"/api/login/createMail".equals(requestURI)
          && !"/api/login/findCodeCheck".equals(requestURI)
          && !"/api/login/updatePwd".equals(requestURI) && !"/speechpro/demo".equals(requestURI)
          && !requestURI.startsWith("/api/sp/demo")) {

        Long sysId = 0L;

        // method 정보
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        String svcNm = proceedingJoinPoint.getTarget().getClass().getName();
        String methodNm = signature.getMethod().getName();
        String rqesterIp = ClientInfoUtil.getIp(request);
        String errorCd = errorStat == 0 ? null : String.valueOf(errorStat);

        SysLogCreateDTO createDTO = SysLogCreateDTO
            .builder()
            .rqesterIp(rqesterIp)
            .rqesterId(sysId)
            .trgetMenuNm(null)
            .svcNm(svcNm)
            .methodNm(methodNm)
            .processSeCd(processSec)
            .processTime(time)
            .errorYn(errorYn)
            .errorCd(errorCd)
            .build();

        sysLogService.create(createDTO);
      }

    } catch (Exception e) {
      e.printStackTrace();
      LOGGER.error("createSysLog exception : {}", e.toString());
    } finally {
      StopWatchContext.clear();
    }
  }

  /**
   * errorLog 생성 (예외 발생 시)
   */
  private void createErrorLog(Throwable ex, JoinPoint joinPoint, double time,
      ProcessSection processSec) {

    try {
      HttpServletRequest request =
          ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
      HttpSession session = request.getSession();
      String requestURI = request.getRequestURI();

      MethodSignature signature = (MethodSignature) joinPoint.getSignature();

      if ("/speechpro/demo".equals(requestURI) || requestURI.startsWith("/api/sp/demo")) {
        // 로그 스킵
        return;
      }
      int errorStat;
      if (ex instanceof ApiException) {
        // ApiException
        ApiException customException = (ApiException) ex;
        ApiResponse<?> apiResponse = customException.getApiResponse();
        errorStat = apiResponse.getStatus();
        LOGGER.error("API Exception : {}", ex.getMessage());
      } else {
        // 일반 예외
        errorStat = HttpStatus.INTERNAL_SERVER_ERROR.value();
        LOGGER.error("Exception : {}", ex.getMessage());
      }

      Long sysId = 0L;

      String svcNm = joinPoint.getTarget().getClass().getName();
      String methodNm = signature.getMethod().getName();
      String rqesterIp = ClientInfoUtil.getIp(request);
      String errorCd = (errorStat == 0) ? null : String.valueOf(errorStat);

      SysLogCreateDTO createDTO = SysLogCreateDTO
          .builder()
          .rqesterIp(rqesterIp)
          .rqesterId(sysId)
          .trgetMenuNm(null)
          .svcNm(svcNm)
          .methodNm(methodNm)
          .processSeCd(processSec)
          .processTime(time)
          .errorYn(ErrorStatus.Y)
          .errorCd(errorCd)
          .build();

      sysLogService.create(createDTO);

    } catch (Exception e) {
      e.printStackTrace();
      LOGGER.error("creatErrorLog exception : {}", e.toString());
    } finally {
      StopWatchContext.clear();
    }
  }

  /**
   * resultObject to ApiResponse
   */
  public ApiResponse<?> resultToResponse(Object object) throws IOException {
    StringWriter stringEmp = new StringWriter();
    objectMapper.writeValue(stringEmp, object);

    Map<String, Object> map =
        objectMapper.readValue(stringEmp.toString(), new TypeReference<Map<String, Object>>() {});
    int status = (int) map.get("status");
    Object data = map.get("data");

    return ApiResponse.of(HttpStatus.valueOf(status), data);
  }
}
