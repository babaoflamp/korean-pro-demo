package com.mk.api.file.application.dto;

import com.mk.api.file.value.DelYn;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
public class FileListParam {

  // File, FileDtl 목록 조회를 위한 Parameter 정의

  private Long fileSeq;

  @Enumerated(EnumType.STRING)
  private DelYn delYn = DelYn.N;

  @Builder
  public FileListParam(Long fileSeq, DelYn delYn) {

    this.fileSeq = fileSeq;
    this.delYn = delYn;
  }
}


