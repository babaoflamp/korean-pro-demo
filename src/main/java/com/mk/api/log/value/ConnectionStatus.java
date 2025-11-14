package com.mk.api.log.value;

import com.mk.common.CodeType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@JsonFormat(shape = Shape.OBJECT)
@Getter
@RequiredArgsConstructor
public enum ConnectionStatus implements CodeType {
  // using loginLog

  I("로그인"), O("로그아웃");

  private final String description;

  @Override
  public String getCode() {
    return name();
  }

}
