package com.mk.api.engine.infrastructure.sp.ko;

import com.mk.api.engine.domain.sp.ko.SpKoAnswer;

public interface SpKoAnswerRepositoryCustom {

  // AI 한국어 학습 지난 결과 - 날짜순
  SpKoAnswer findPreviousBySysIdAndKoId(Long sysId, Long koId);

}
