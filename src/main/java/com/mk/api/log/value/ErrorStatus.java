package com.mk.api.log.value;

import com.mk.common.CodeType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = Shape.OBJECT)
public enum ErrorStatus implements CodeType {
  // using sysLog
  // com_cd_group.ERR_YN

  Y("오류"), N("정상");

  private final String description;

  @Override
  public String getCode() {
    return name();
  }
}
