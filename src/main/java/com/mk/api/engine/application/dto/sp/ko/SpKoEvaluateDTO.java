package com.mk.api.engine.application.dto.sp.ko;

import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class SpKoEvaluateDTO {

  private String id;
  private String text;

  @JsonProperty("error code")
  private int errorCode;        // success : 0

  @JsonProperty("syll ltrs")
  private String syllLtrs;      // 글자단위 구분자('_')

  @JsonProperty("syll phns")
  private String syllPhns;      // 글자단위 발음열

  private String fst;           // 생성된 모델

  @JsonProperty("wav usr")
  private String wavUsr;        // audio base64

  private Result result;             // 평가결과
  private LocalDateTime submittedAt; // 제출일자

  @Getter
  @NoArgsConstructor
  public static class Result {
    private Fluency fluency;
    private Quality quality;
    private Settings settings;
  }

  @Getter
  @NoArgsConstructor
  public static class Fluency {
    @JsonProperty("articulation length")
    private double articulationLength;

    @JsonProperty("articulation rate")
    private double articulationRate;

    @JsonProperty("correct syllable count")
    private int correctSyllableCount;

    @JsonProperty("correct word count")
    private int correctWordCount;

    private double duration;
    private double end;

    @JsonProperty("sil count")
    private int silCount;

    @JsonProperty("sil count between sentences")
    private int silCountBetweenSentences;

    @JsonProperty("sil count between words")
    private int silCountBetweenWords;

    @JsonProperty("sil error rate between sentences")
    private double silErrorRateBetweenSentences;

    @JsonProperty("sil error rate within sentence")
    private double silErrorRateWithinSentence;

    @JsonProperty("sil_r_within sentence")
    private List<Double> silRWithInSentence;

    @JsonProperty("sil_t_within sentence")
    private List<Double> silTWithInSentence;

    @JsonProperty("speech rate")
    private double speechRate;

    private double start;

    @JsonProperty("syllable count")
    private int syllableCount;

    @JsonProperty("word count")
    private int wordCount;

    @JsonProperty("raw_pause_diff")
    private List<Double> rawPauseDiff;

    @JsonProperty("user_sil_error")
    private List<Double> userSilError;
  }

  @Getter
  @NoArgsConstructor
  public static class Quality {
    private List<Sentence> sentences;

    @Getter
    @NoArgsConstructor
    public static class Sentence {
      private double score;
      private String text;

      @JsonProperty("time duration")
      private double timeDuration;

      @JsonProperty("time start")
      private double timeStart;

      private List<Word> words;
    }

    @Getter
    @NoArgsConstructor
    public static class Word {
      private double score;
      private String text;

      @JsonProperty("time duration")
      private double timeDuration;

      @JsonProperty("time start")
      private double timeStart;

      private List<Syllable> syll;
    }

    @Getter
    @NoArgsConstructor
    public static class Syllable {
      private double score;
      private String text;

      @JsonProperty("time duration")
      private double timeDuration;

      @JsonProperty("time start")
      private double timeStart;

      private List<Phone> phones;
    }

    @Getter
    @NoArgsConstructor
    public static class Phone {
      private double score;
      private String text;
      private String symbol;
      private int stress;

      @JsonProperty("time duration")
      private double timeDuration;

      @JsonProperty("time start")
      private double timeStart;
    }
  }

  @Getter
  @NoArgsConstructor
  public static class Settings {
    @JsonProperty("bias fluent")
    private double biasFluent;

    @JsonProperty("bias learner")
    private double biasLearner;

    @JsonProperty("bias natural")
    private double biasNatural;

    @JsonProperty("correct threshold")
    private double correctThreshold;

    @JsonProperty("silence threshold")
    private double silenceThreshold;

    @JsonProperty("silence threshold between sentence")
    private double silenceThresholdBetweenSentence;

    @JsonProperty("silence threshold within sentence")
    private double silenceThresholdWithinSentence;
  }

}
