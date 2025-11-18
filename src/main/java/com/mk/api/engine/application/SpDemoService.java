package com.mk.api.engine.application;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mk.api.engine.application.dto.sp.ko.SpKoEvaluateDTO;
import com.mk.api.engine.application.dto.sp.ko.SpKoModelDTO;
import com.mk.api.engine.application.dto.sp.ko.SpKoQuestionDTO;
import com.mk.api.engine.domain.sp.ko.SpKoAnswer;
import com.mk.api.engine.domain.sp.ko.SpKoQuestion;
import com.mk.api.engine.infrastructure.sp.ko.SpKoAnswerRepository;
import com.mk.api.engine.infrastructure.sp.ko.SpKoQuestionRepository;
import com.mk.api.file.application.dto.FileDtlCreateDTO;
import com.mk.api.file.domain.File;
import com.mk.api.file.domain.FileDtl;
import com.mk.api.file.infrastructure.FileDtlRepository;
import com.mk.api.file.infrastructure.FileRepository;
import com.mk.common.ApiResponse;
import com.mk.common.Base64ToFileConverter;
import com.mk.common.FileUtil;
import com.mk.common.HttpUtil;
import lombok.RequiredArgsConstructor;

/*
 * 1. gtp : G2P(Grapheme-to-Phoneme, 문자를 발음기호(=음소)로 변환)와 음절 분리(Syllabification) 2. model : 발음 기호 모델
 * 생성 3-1. score : wav 파일로 발음평가 3-2. scorejson : base64로 json 전송하여 평가
 *
 * ※ db에 sentence를 텍스트 복사하여 넣었을 시 직접 입력한 스페이스 공백이랑은 리눅스 서버에서 다르게 보일 수 있음 => 변환처리
 */
@Service
@RequiredArgsConstructor
public class SpDemoService {

  private static Logger LOGGER = LoggerFactory.getLogger(SpDemoService.class);

  private final SpKoQuestionRepository spKoQuestionRepository;

  private final SpKoAnswerRepository spKoAnswerRepository;

  private final FileRepository fileRepository;

  private final FileDtlRepository fileDtlRespository;

  private final Base64ToFileConverter base64ToFileConverter;

  private final FileUtil fileUtil;

  private final ObjectMapper mapper;

  @Value("${api.speechpro_kr.url}")
  String baseUrl;

  private static final String GTP = "/gtp";
  private static final String MODEL = "/model";
  private static final String SCORE_JSON = "/scorejson";

  private final static String DIR_NM = "speechpro-ko";

  /**
   * AI 한국어 학습 - 문제 목록
   *
   * @return ApiResponse<List<SpKoQuestionDTO>>
   */
  public ApiResponse<List<SpKoQuestionDTO>> findAll() {

    try {

      List<SpKoQuestionDTO> list = spKoQuestionRepository.findAllByDemo();

      return list.isEmpty() ? ApiResponse.of(HttpStatus.NOT_FOUND, null)
          : ApiResponse.of(HttpStatus.OK, list);

    } catch (Exception e) {
      LOGGER.error(e.getMessage());
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
  }

  /**
   * AI 한국어 학습 - 문제 1개 정보 조회
   *
   * @param 문제 id
   * @return ApiResponse<SpKoQuestionDTO>
   */
  public ApiResponse<SpKoQuestionDTO> findById(String koId) {

    try {

      Optional<SpKoQuestionDTO> question = Optional
          .ofNullable(spKoQuestionRepository.findOneByDemo(Long.valueOf(koId)));

      System.out.println(question.isPresent());

      return question
          .map(q -> ApiResponse.of(HttpStatus.OK, question.get()))
          .orElseGet(() -> ApiResponse.of(HttpStatus.NOT_FOUND, null));

    } catch (Exception e) {
      LOGGER.error(e.getMessage());
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
  }

  /**
   * AI 한국어 학습 - 지난 결과
   *
   * @param 문제 id
   * @return ApiResponse<SpKoAnswer>
   */
  public ApiResponse<SpKoAnswer> findPrevious(String id) {

    try {
      Optional<SpKoAnswer> answer = Optional
          .ofNullable(spKoAnswerRepository
              .findPreviousBySysIdAndKoId(null, Long.valueOf(id)));

      return answer
          .map(q -> ApiResponse.of(HttpStatus.OK, answer.get()))
          .orElseGet(() -> ApiResponse.of(HttpStatus.NOT_FOUND, null));

    } catch (Exception e) {
      LOGGER.error(e.getMessage());
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

  }

  /**
   * speechpro(ko) 발음 평가
   *
   * @param SpKoEvaluateDTO
   * @return ApiResponse
   */
  public ApiResponse<?> createEvaluate(SpKoEvaluateDTO evaluateDTO) {
    try {

      Optional<SpKoQuestion> question = spKoQuestionRepository.findById(Long.parseLong(evaluateDTO.getId()));

      if (!question.isPresent()) {
        return ApiResponse.of(HttpStatus.NOT_FOUND, null);
      }

      Map<String, Object> header = new HashMap<>();
      header.put("Content-Type", "application/json");

      Map<String, Object> body = new HashMap<>();
      body.put("id", evaluateDTO.getId());
      body.put("text", normalizeSpaces(question.get().getSentence()));
      body.put("syll ltrs", question.get().getSyllLtrs());
      body.put("syll phns", question.get().getSyllPhns());
      body.put("fst", question.get().getFst());
      body.put("wav usr", evaluateDTO.getWavUsr());

      String result = HttpUtil.executeRequest("POST", baseUrl + SCORE_JSON, header, body);
      JsonNode node = mapper.readTree(result);

      String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
      // 녹음 파일 저장
      String fileName = "demo_" + currentDateTime;
      MultipartFile file = base64ToFileConverter.base64ToFile(evaluateDTO.getWavUsr(), fileName + ".wav");

      // 파일 부모
      File fileEntity = File.builder().rgtrSysId(99999999999999999L).build();
      fileEntity.create();

      fileRepository.save(fileEntity);

      FileDtlCreateDTO fileCreateDTO = FileDtlCreateDTO.builder().dirNm(DIR_NM).rgtrSysId(99999999999999999L).build();

      FileDtlCreateDTO uploadResult = fileUtil.uploadFile(fileCreateDTO, file);
      FileDtl fileDtlEntity = uploadResult.toEntity(fileEntity);
      fileDtlEntity.create();

      fileDtlRespository.save(fileDtlEntity);

      // 평가 결과 저장
      SpKoAnswer answer = SpKoAnswer
          .builder()
          .sysId(99999999999999999L)
          .spKoQuestion(question.get())
          .result(node)
          .fileSeq(fileEntity.getFileSeq())
          .build();

      spKoAnswerRepository.save(answer);

      return ApiResponse.of(HttpStatus.OK, answer);

    } catch (Exception e) {
      LOGGER.error(e.getMessage());
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

  }

  /**
   * speechpro(kr) 문장 발음 기호 & 모델 생성
   *
   * @params KoreanProModelDTO
   * @return ApiResponse
   */
  public ApiResponse<SpKoModelDTO> createModel(SpKoModelDTO requestDTO) {

    try {
      Map<String, Object> header = new HashMap<>();
      header.put("Content-Type", "application/json");

      Map<String, Object> body = new HashMap<>();
      body.put("id", requestDTO.getId());
      body.put("text", requestDTO.getText());

      // 1. 문장 발음 생성
      String gtpResult = HttpUtil.executeRequest("POST", baseUrl + GTP, header, body);
      SpKoModelDTO gtpDTO = mapper.readValue(gtpResult, SpKoModelDTO.class);

      body.put("syll ltrs", gtpDTO.getSyllLtrs());
      body.put("syll phns", gtpDTO.getSyllPhns());

      // 2. 문장 모델 생성
      String modelResult = HttpUtil.executeRequest("POST", baseUrl + MODEL, header, body);
      SpKoModelDTO modelDTO = mapper.readValue(modelResult, SpKoModelDTO.class);

      modelDTO = modelDTO
          .toBuilder()
          .text(gtpDTO.getText())
          .syllLtrs(gtpDTO.getSyllLtrs())
          .syllPhns(gtpDTO.getSyllPhns())
          .build();

      return ApiResponse.of(HttpStatus.OK, modelDTO);

    } catch (Exception e) {
      LOGGER.error(e.getMessage());
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
  }

  public static String normalizeSpaces(String input) {
    if (input == null) {
      return null;
    }

    return input
        .replace("\u00A0", " ") // NBSP (Non-Breaking Space, U+00A0) → 일반 공백
        .replace("\u2002", " ") // En Space (U+2002) → 일반 공백
        .replace("\u2003", " ") // Em Space (U+2003) → 일반 공백
        .replace("\u2009", " ") // Thin Space (U+2009) → 일반 공백
        .replace("\t", " ") // Tab (\t) → 일반 공백
        .replaceAll(" +", " "); // 연속된 공백을 하나의 공백으로 축소
  }

  /**
   * 문장(Question)의 발음기호 및 모델(fst)을 외부 엔진에 재요청하여 DB에 갱신한다.
   *
   * @param koId 문제 ID
   * @return 갱신된 문제 정보
   */
  public ApiResponse<SpKoQuestionDTO> refreshQuestionModel(Long koId) {
    try {
      if (koId == null) {
        return ApiResponse.of(HttpStatus.BAD_REQUEST, null);
      }
      Optional<SpKoQuestion> optional = spKoQuestionRepository.findById(koId);
      if (optional.isEmpty()) {
        return ApiResponse.of(HttpStatus.NOT_FOUND, null);
      }

      SpKoQuestion question = optional.get();

      // 1) GTP 호출로 syll_ltrs, syll_phns 갱신 데이터 획득
      Map<String, Object> header = new HashMap<>();
      header.put("Content-Type", "application/json");

      Map<String, Object> body = new HashMap<>();
      body.put("id", String.valueOf(question.getKoId()));
      body.put("text", normalizeSpaces(question.getSentence()));

      String gtpResult = HttpUtil.executeRequest("POST", baseUrl + GTP, header, body);
      SpKoModelDTO gtpDTO = mapper.readValue(gtpResult, SpKoModelDTO.class);

      // 2) MODEL 호출로 fst 생성
      body.put("syll ltrs", gtpDTO.getSyllLtrs());
      body.put("syll phns", gtpDTO.getSyllPhns());
      String modelResult = HttpUtil.executeRequest("POST", baseUrl + MODEL, header, body);
      SpKoModelDTO modelDTO = mapper.readValue(modelResult, SpKoModelDTO.class);

      // 3) 엔티티 갱신 및 저장
      question.updatePhonemeAndModel(gtpDTO.getSyllLtrs(), gtpDTO.getSyllPhns(), modelDTO.getFst());
      spKoQuestionRepository.save(question);

      return ApiResponse.of(HttpStatus.OK, SpKoQuestionDTO.of(question));
    } catch (Exception e) {
      LOGGER.error(e.getMessage());
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
  }

  /**
   * 모든 문장(Question)에 대해 발음기호/모델을 재생성하여 DB에 반영
   *
   * @return 갱신 성공 건수
   */
  public ApiResponse<Integer> refreshAllQuestionModels() {
    try {
      List<SpKoQuestion> all = spKoQuestionRepository.findAll();
      int success = 0;
      for (SpKoQuestion q : all) {
        ApiResponse<SpKoQuestionDTO> res = refreshQuestionModel(q.getKoId());
        if (res.getStatus() >= 200 && res.getStatus() < 300) {
          success++;
        }
      }
      return ApiResponse.of(HttpStatus.OK, success);
    } catch (Exception e) {
      LOGGER.error(e.getMessage());
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, 0);
    }
  }

}
