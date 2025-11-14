package com.mk.api.file.application.dto;

import java.time.LocalDateTime;
import com.mk.api.file.domain.File;
import com.mk.api.file.domain.FileDtl;
import com.mk.api.file.value.DelYn;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileUpdateDTO {

  // File, FileDtl Update를 위한 DTO

  private Long fileSeq;
  private Long fileDtlSeq;

  @Enumerated(EnumType.STRING)
  private DelYn delYn;

  private LocalDateTime mdfcnDt;
  private Long mdfrSysId;

  private String dirNm;

  private Long[] fileDtlSeqArr;

  public FileDtl toEntity(File file) {
    return FileDtl.builder().file(file).fileDtlSeq(fileDtlSeq).mdfrSysId(mdfrSysId).build();
  }

  public void delete() {
    this.delYn = DelYn.Y;
    this.mdfcnDt = LocalDateTime.now();
  }
}
