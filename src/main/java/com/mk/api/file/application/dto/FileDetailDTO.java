package com.mk.api.file.application.dto;

import java.time.LocalDateTime;
import com.mk.api.file.domain.FileDtl;
import com.mk.api.file.value.DelYn;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@AllArgsConstructor
public class FileDetailDTO {

  // File, FileDtl 상세 조회를 위한 DTO

  private Long fileSeq;
  private Long fileDtlSeq;

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

  private String rgtrSysNm;

  public static FileDetailDTO of(FileDtl fileDtl) {
    return FileDetailDTO
        .builder()
        .fileSeq(fileDtl.getFile().getFileSeq())
        .fileDtlSeq(fileDtl.getFileDtlSeq())
        .fileSn(fileDtl.getFileSn())
        .fileStrePath(fileDtl.getFileStrePath())
        .streFileNm(fileDtl.getStreFileNm())
        .orignlFileNm(fileDtl.getOrignlFileNm())
        .fileExtsn(fileDtl.getFileExtsn())
        .fileSize(fileDtl.getFileSize())
        .delYn(fileDtl.getDelYn())
        .regDt(fileDtl.getRegDt())
        .rgtrSysId(fileDtl.getRgtrSysId())
        .mdfcnDt(fileDtl.getMdfcnDt())
        .mdfrSysId(fileDtl.getMdfrSysId())
        .build();
  }

}
