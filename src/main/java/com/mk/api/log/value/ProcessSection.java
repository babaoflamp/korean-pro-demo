package com.mk.api.log.value;

import com.mk.common.CodeType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = Shape.OBJECT)
public enum ProcessSection implements CodeType {
  // using sysLog
  // com_cd_group.PRCS_SE

  C("생성"), R("조회"), U("수정"), D("삭제");

  private final String description;

  @Override
  public String getCode() {
    return name();
  }

}
