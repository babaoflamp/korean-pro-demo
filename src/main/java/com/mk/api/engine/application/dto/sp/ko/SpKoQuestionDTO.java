package com.mk.api.engine.application.dto.sp.ko;

import com.mk.api.engine.domain.sp.ko.SpKoQuestion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder(toBuilder = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SpKoQuestionDTO {

  private Long koId;
  private int order;
  private String sentence;
  private String syllLtrs;
  private String syllPhns;
  private boolean hasAnswered;
  private String fst;

  public static SpKoQuestionDTO of(SpKoQuestion entity) {

    return SpKoQuestionDTO
        .builder()
        .koId(entity.getKoId())
        .order(entity.getOrder())
        .sentence(entity.getSentence())
        .syllLtrs(entity.getSyllLtrs())
        .syllPhns(entity.getSyllPhns())
        .fst(entity.getFst())
        .build();
  }

  @Builder
  public SpKoQuestionDTO(Long koId, int order, String sentence, String syllLtrs, String syllPhns,
      boolean hasAnswered) {
    this.koId = koId;
    this.order = order;
    this.sentence = sentence;
    this.syllLtrs = syllLtrs;
    this.syllPhns = syllPhns;
    this.hasAnswered = hasAnswered;
  }

}
