package com.mk.api.engine.domain.sp.ko;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class SpKoQuestion {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long koId;

  @Column(name = "\"order\"")
  private int order;

  private String sentence;
  private String syllLtrs;
  private String syllPhns;
  private String fst;

  @Builder
  public SpKoQuestion(long koId, int order, String sentence, String syllLtrs, String syllPhns,
      String fst) {
    this.koId = koId;
    this.order = order;
    this.sentence = sentence;
    this.syllLtrs = syllLtrs;
    this.syllPhns = syllPhns;
    this.fst = fst;
  }

  /**
   * Update syllable letters, syllable phonemes and model fields.
   *
   * @param syllLtrs 글자단위 구분자('_')
   * @param syllPhns 글자단위 발음열
   * @param fst      생성된 모델 식별자
   */
  public void updatePhonemeAndModel(String syllLtrs, String syllPhns, String fst) {
    this.syllLtrs = syllLtrs;
    this.syllPhns = syllPhns;
    this.fst = fst;
  }

}
