package com.mk.api.engine.domain.sp.ko;

import java.time.LocalDateTime;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class SpKoAnswer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long answerId;

  private Long sysId;

  @ManyToOne
  @JoinColumn(name = "koId")
  private SpKoQuestion spKoQuestion;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  private JsonNode result;

  private Long fileSeq;

  private LocalDateTime submittedAt;

  @Builder
  public SpKoAnswer(Long answerId, Long sysId, SpKoQuestion spKoQuestion, JsonNode result, Long fileSeq,
      LocalDateTime submittedAt) {
    this.answerId = answerId;
    this.sysId = sysId;
    this.spKoQuestion = spKoQuestion;
    this.result = result;
    this.fileSeq = fileSeq;
    this.submittedAt = LocalDateTime.now();
  }


}
