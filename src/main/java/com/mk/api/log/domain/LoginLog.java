package com.mk.api.log.domain;

import java.time.LocalDateTime;
import com.mk.api.log.value.ConnectionStatus;
import com.mk.api.log.value.ErrorStatus;
import jakarta.persistence.Column;
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
@Table(name = "com_loginlog")
@Getter
@NoArgsConstructor
public class LoginLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long logSeq; // 로그seq PK

  @Enumerated(EnumType.STRING)
  private ConnectionStatus conectSttusCd; // 접속상태코드 (로그인/로그아웃)
  private Long conectId; // 접속자 sysid
  private String conectIp; // 접속 IP
  private String conectOs; // 접속 OS
  private String conectBrowser; // 접속 브라우저

  @Enumerated(EnumType.STRING)
  private ErrorStatus errorCd; // 오류코드

  @Column(name = "log_dt", insertable = false)
  private LocalDateTime logDt; // 로그일시

  @Builder
  public LoginLog(Long logSeq, ConnectionStatus conectSttusCd, Long conectId, String conectIp,
      String conectOs, String conectBrowser, ErrorStatus errorCd, LocalDateTime logDt) {
    this.logSeq = logSeq;
    this.conectSttusCd = conectSttusCd;
    this.conectId = conectId;
    this.conectIp = conectIp;
    this.conectOs = conectOs;
    this.conectBrowser = conectBrowser;
    this.errorCd = errorCd;
    this.logDt = LocalDateTime.now();
  }

}
