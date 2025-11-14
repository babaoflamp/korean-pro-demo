package com.mk.api.engine.infrastructure.sp.ko;

import java.util.List;
import com.mk.api.engine.application.dto.sp.ko.SpKoQuestionDTO;

public interface SpKoQuestionRepositoryCustom {

  // AI 한국어학습 문제 목록 - 로그인 사용자 답변 여부 포함
  List<SpKoQuestionDTO> findAllBySysIdWithAnswerStatus(Long sysId, boolean showFst);

  // AI 한국어 학습 문제 1건 조회
  SpKoQuestionDTO findOneBySysIdWithAnswerStatus(Long sysId, Long koId, boolean showFst);

  List<SpKoQuestionDTO> findAllByDemo();

  SpKoQuestionDTO findOneByDemo(Long koId);

}
