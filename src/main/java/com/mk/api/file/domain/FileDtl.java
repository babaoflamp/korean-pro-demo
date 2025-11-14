package com.mk.api.file.domain;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mk.api.file.value.DelYn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "com_file_dtl")
@Getter
@NoArgsConstructor
public class FileDtl {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long fileDtlSeq;

  @ManyToOne
  @JoinColumn(name = "file_seq")
  @JsonIgnore
  private File file;

  private int fileSn;
  private String fileStrePath;
  private String streFileNm;
  private String orignlFileNm;
  private String fileExtsn;
  private Long fileSize;

  @Enumerated(EnumType.STRING)
  private DelYn delYn;

  private LocalDateTime regDt;
  private Long rgtrSysId;
  private LocalDateTime mdfcnDt;
  private Long mdfrSysId;

  @Builder
  public FileDtl(Long fileDtlSeq, File file, int fileSn, String fileStrePath, String streFileNm,
      String orignlFileNm, String fileExtsn, Long fileSize, DelYn delYn, LocalDateTime regDt,
      Long rgtrSysId, LocalDateTime mdfcnDt, Long mdfrSysId) {

    this.fileDtlSeq = fileDtlSeq;
    this.file = file;
    this.fileSn = fileSn;
    this.fileStrePath = fileStrePath;
    this.streFileNm = streFileNm;
    this.orignlFileNm = orignlFileNm;
    this.fileExtsn = fileExtsn;
    this.fileSize = fileSize;
    this.delYn = DelYn.N;
    this.regDt = regDt;
    this.rgtrSysId = rgtrSysId;
    this.mdfcnDt = mdfcnDt;
    this.mdfrSysId = mdfrSysId;
  }

  public void create() {
    this.delYn = DelYn.N;
    this.regDt = LocalDateTime.now();
    this.mdfcnDt = LocalDateTime.now();
  }

  public void delete() {
    this.delYn = DelYn.Y;
    this.mdfcnDt = LocalDateTime.now();
  }
}

