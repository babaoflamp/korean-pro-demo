package com.mk.api.engine.infrastructure.sp.ko;

import java.util.List;
import com.mk.api.engine.domain.sp.ko.QSpKoAnswer;
import com.mk.api.engine.domain.sp.ko.SpKoAnswer;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SpKoAnswerRepositoryCustomImpl implements SpKoAnswerRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public SpKoAnswer findPreviousBySysIdAndKoId(Long sysId, Long koId) {
    QSpKoAnswer answer = QSpKoAnswer.spKoAnswer;

    List<SpKoAnswer> list = queryFactory
        .selectFrom(answer)
        .where(answer.sysId.eq(sysId).and(answer.spKoQuestion.koId.eq(koId)))
        .orderBy(answer.submittedAt.desc())
        .limit(1)
        .fetch();

    SpKoAnswer result = list.isEmpty() ? null : list.get(0);

    return result;
  }


}
