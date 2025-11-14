package com.mk.api.engine.application.dto.sp.ko;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class SpKoModelDTO {

  private String id;

  @JsonProperty("error code")
  private int errorCode;        // success : 0
  private String text;

  @JsonProperty("syll ltrs")
  private String syllLtrs;      // 글자단위 구분자'_'

  @JsonProperty("syll phns")
  private String syllPhns;      // 글자단위 발음열

  private String fst;           // 생성된 모델

}
