package com.mk.api.log.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "com_weblog")
@Getter
@NoArgsConstructor
public class WebLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long logSeq; // 로그seq PK

  private LocalDate occrrncDe; // 발생일자
  private LocalTime occrrncTime; // 발생시간

  private String url; // url
  private Long rqesterId; // 요청자 ID
  private String rqesterIp; // 요청자 IP

  @Builder
  public WebLog(Long logSeq, LocalDate occrrncDe, LocalTime occrrncTime, String url, Long rqesterId,
      String rqesterIp) {
    this.logSeq = logSeq;
    this.occrrncDe = LocalDate.now();
    this.occrrncTime = LocalTime.now();
    this.url = url;
    this.rqesterId = rqesterId;
    this.rqesterIp = rqesterIp;
  }

}
