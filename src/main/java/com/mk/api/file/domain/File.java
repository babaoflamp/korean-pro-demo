package com.mk.api.file.domain;

import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "com_file")
@Getter
@NoArgsConstructor
public class File {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long fileSeq;

  private LocalDateTime regDt;
  private Long rgtrSysId;

  // 부모->자식 저장/삭제 전파, 엔티티가 부모없이 존재할 수 없도록 설정
  @OneToMany(mappedBy = "file")
  private List<FileDtl> fileDtls;

  @Builder
  public File(Long fileSeq, LocalDateTime regDt, Long rgtrSysId) {
    this.fileSeq = fileSeq;
    this.regDt = regDt;
    this.rgtrSysId = rgtrSysId;
  }

  public void create() {
    this.regDt = LocalDateTime.now();
  }
}
