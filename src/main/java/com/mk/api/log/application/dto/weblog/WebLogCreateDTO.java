package com.mk.api.log.application.dto.weblog;

import java.time.LocalDate;
import java.time.LocalTime;
import com.mk.api.log.domain.WebLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class WebLogCreateDTO {

  private LocalDate occrrncDe;
  private LocalTime occrrncTime;
  private String url;
  private String rqesterIp;
  private Long rqesterId;

  // dto to entity
  public WebLog toEntity() {
    return WebLog
        .builder()
        .occrrncDe(occrrncDe)
        .occrrncTime(occrrncTime)
        .url(url)
        .rqesterIp(rqesterIp)
        .rqesterId(rqesterId)
        .build();
  }

}
