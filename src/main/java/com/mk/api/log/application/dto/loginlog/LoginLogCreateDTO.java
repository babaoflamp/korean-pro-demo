package com.mk.api.log.application.dto.loginlog;

import com.mk.api.log.domain.LoginLog;
import com.mk.api.log.value.ConnectionStatus;
import com.mk.api.log.value.ErrorStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class LoginLogCreateDTO {

  @Enumerated(EnumType.STRING)
  private ConnectionStatus conectSttusCd;

  private Long conectId;
  private String conectIp;
  private String conectOs;
  private String conectBrowser;

  @Enumerated(EnumType.STRING)
  private ErrorStatus errorCd;

  public LoginLog toEntity() {
    return LoginLog
        .builder()
        .conectSttusCd(conectSttusCd)
        .conectId(conectId)
        .conectIp(conectIp)
        .conectOs(conectOs)
        .conectBrowser(conectBrowser)
        .errorCd(errorCd)
        .build();
  }
}
