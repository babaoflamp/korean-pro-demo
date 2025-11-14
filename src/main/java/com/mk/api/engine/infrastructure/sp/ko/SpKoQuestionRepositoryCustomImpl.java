package com.mk.api.engine.infrastructure.sp.ko;

import java.util.List;
import com.mk.api.engine.application.dto.sp.ko.SpKoQuestionDTO;
import com.mk.api.engine.domain.sp.ko.QSpKoAnswer;
import com.mk.api.engine.domain.sp.ko.QSpKoQuestion;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SpKoQuestionRepositoryCustomImpl implements SpKoQuestionRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<SpKoQuestionDTO> findAllBySysIdWithAnswerStatus(Long sysId, boolean showFst) {

    QSpKoQuestion question = QSpKoQuestion.spKoQuestion;
    QSpKoAnswer answer = QSpKoAnswer.spKoAnswer;

    BooleanExpression hasAnswered = JPAExpressions
        .selectOne()
        .from(answer)
        .where(answer.spKoQuestion.koId.eq(question.koId))
        .exists();

    List<SpKoQuestionDTO> result = queryFactory
        .select(Projections
            .constructor(SpKoQuestionDTO.class, question.koId.longValue(),
                question.order.intValue(), question.sentence.stringValue(), question.syllLtrs,
                question.syllPhns, hasAnswered))
        .from(question)
        .leftJoin(answer)
        .on(question.koId.eq(answer.spKoQuestion.koId).and(answer.sysId.eq(sysId)))
        .groupBy(question.koId, question.order, question.sentence, question.syllLtrs,
            question.syllPhns)
        .orderBy(question.order.asc())
        .fetch();

    if (showFst) {
      result.stream().map(q -> {
        return q.toBuilder().fst(question.fst.toString()).build();
      });
    }

    return result;

  }

  @Override
  public SpKoQuestionDTO findOneBySysIdWithAnswerStatus(Long sysId, Long koId, boolean showFst) {
    QSpKoQuestion question = QSpKoQuestion.spKoQuestion;
    QSpKoAnswer answer = QSpKoAnswer.spKoAnswer;

    BooleanExpression hasAnswered = JPAExpressions
        .selectOne()
        .from(answer)
        .where(answer.spKoQuestion.koId.eq(question.koId))
        .exists();

    SpKoQuestionDTO result = queryFactory
        .select(Projections
            .constructor(SpKoQuestionDTO.class, question.koId.longValue(),
                question.order.intValue(), question.sentence.stringValue(), question.syllLtrs,
                question.syllPhns, hasAnswered))
        .from(question)
        .leftJoin(answer)
        .on(question.koId.eq(answer.spKoQuestion.koId).and(answer.sysId.eq(sysId)))
        .where(question.koId.eq(koId))
        .groupBy(question.koId, question.order, question.sentence, question.syllLtrs,
            question.syllPhns)
        .fetchOne();

    if (showFst) {
      result.toBuilder().fst(question.fst.toString()).build();
    }

    return result;
  }

  @Override
  public List<SpKoQuestionDTO> findAllByDemo() {

    QSpKoQuestion question = QSpKoQuestion.spKoQuestion;
    QSpKoAnswer answer = QSpKoAnswer.spKoAnswer;

    BooleanExpression hasAnswered = JPAExpressions
        .selectOne()
        .from(answer)
        .where(answer.spKoQuestion.koId.eq(question.koId))
        .exists();

    List<SpKoQuestionDTO> result = queryFactory
        .select(Projections
            .constructor(SpKoQuestionDTO.class, question.koId.longValue(),
                question.order.intValue(), question.sentence.stringValue(), question.syllLtrs,
                question.syllPhns, hasAnswered))
        .from(question)
        .leftJoin(answer)
        .on(question.koId.eq(answer.spKoQuestion.koId))
        .groupBy(question.koId, question.order, question.sentence, question.syllLtrs,
            question.syllPhns)
        .orderBy(question.order.asc())
        .fetch();

    return result;

  }

  @Override
  public SpKoQuestionDTO findOneByDemo(Long koId) {

    QSpKoQuestion question = QSpKoQuestion.spKoQuestion;
    QSpKoAnswer answer = QSpKoAnswer.spKoAnswer;

    BooleanExpression hasAnswered = JPAExpressions
        .selectOne()
        .from(answer)
        .where(answer.spKoQuestion.koId.eq(question.koId))
        .exists();

    SpKoQuestionDTO result = queryFactory
        .select(Projections
            .constructor(SpKoQuestionDTO.class, question.koId.longValue(),
                question.order.intValue(), question.sentence.stringValue(), question.syllLtrs,
                question.syllPhns, hasAnswered))
        .from(question)
        .leftJoin(answer)
        .on(question.koId.eq(answer.spKoQuestion.koId))
        .where(question.koId.eq(koId))
        .groupBy(question.koId, question.order, question.sentence, question.syllLtrs,
            question.syllPhns)
        .fetchOne();

    return result;
  }

}
