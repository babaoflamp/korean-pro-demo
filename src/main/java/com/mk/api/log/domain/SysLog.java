package com.mk.api.log.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import com.mk.api.log.value.ErrorStatus;
import com.mk.api.log.value.ProcessSection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "com_syslog")
@Getter
@NoArgsConstructor
public class SysLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long logSeq; // 로그seq PK

  private LocalDate occrrncDe; // 발생일자
  private LocalTime occrrncTime; // 발생시간
  private String rqesterIp; // 요청자 IP
  private Long rqesterId; // 요청자 ID
  private String trgetMenuNm; // 대상메뉴명
  private String svcNm; // 서비스명
  private String methodNm; // 메서드명

  @Enumerated(EnumType.STRING)
  private ProcessSection processSeCd; // 처리구분코드 com_cd_dtl TB [PRCS_SE]
  private Double processTime; // 처리 시간

  @Enumerated(EnumType.STRING)
  private ErrorStatus errorYn; // 오류여부 com_cd_dtl TB [ERR_YN]
  private String errorCd; // 오류코드

  @Builder
  public SysLog(Long logSeq, LocalDate occrrncDe, LocalTime occrrncTime, String rqesterIp, Long rqesterId,
      String trgetMenuNm, String svcNm, String methodNm, ProcessSection processSeCd,
      Double processTime, ErrorStatus errorYn, String errorCd) {
    this.logSeq = logSeq;
    this.occrrncDe = LocalDate.now();
    this.occrrncTime = LocalTime.now();
    this.rqesterIp = rqesterIp;
    this.rqesterId = rqesterId;
    this.trgetMenuNm = trgetMenuNm;
    this.svcNm = svcNm;
    this.methodNm = methodNm;
    this.processSeCd = processSeCd;
    this.processTime = processTime;
    this.errorYn = errorYn;
    this.errorCd = errorCd;
  }

}
