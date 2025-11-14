package com.mk.api.file.value;

import com.mk.common.CodeType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DelYn implements CodeType {

  Y("삭제"), N("미삭제");

  private final String description;

  @Override
  public String getCode() {
    return name();
  }
}

