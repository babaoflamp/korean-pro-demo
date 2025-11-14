package com.mk.api.file.application.dto;

import java.time.LocalDateTime;
import com.mk.api.file.domain.File;
import com.mk.api.file.domain.FileDtl;
import com.mk.api.file.value.DelYn;
import groovy.transform.ToString;
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
@ToString
public class FileDtlCreateDTO {

  // File, FileDtl Create를 위한 DTO

  private Long fileSeq;

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

  private String dirNm;

  public FileDtl toEntity(File file) {
    return FileDtl
        .builder()
        .file(file)
        .fileSn(fileSn)
        .fileStrePath(fileStrePath)
        .streFileNm(streFileNm)
        .orignlFileNm(orignlFileNm)
        .fileExtsn(fileExtsn)
        .fileSize(fileSize)
        .rgtrSysId(rgtrSysId)
        .mdfrSysId(mdfrSysId)
        .build();
  }

}
