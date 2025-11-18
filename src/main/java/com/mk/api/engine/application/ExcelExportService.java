package com.mk.api.engine.application;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.mk.api.engine.application.dto.sp.ko.ExcelExportRequestDTO;
import com.mk.api.engine.domain.sp.ko.SpKoAnswer;
import com.mk.api.engine.infrastructure.sp.ko.SpKoAnswerRepository;
import com.mk.api.file.domain.File;
import com.mk.api.file.domain.FileDtl;
import com.mk.api.file.infrastructure.FileDtlRepository;
import com.mk.api.file.infrastructure.FileRepository;
import com.mk.api.file.value.DelYn;
import com.mk.config.logging.NoLogging;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExcelExportService {

  private static Logger LOGGER = LoggerFactory.getLogger(ExcelExportService.class);

  private final SpKoAnswerRepository spKoAnswerRepository;
  private final FileRepository fileRepository;
  private final FileDtlRepository fileDtlRepository;

  @Value("${spring.servlet.multipart.location}")
  private String UPLOAD_PATH;

  private static final String DIR_NM = "excel-export";

  /**
   * Excel 파일 생성 및 저장
   *
   * @param requestDTO userName과 answerIds
   * @return FileDtl 저장된 Excel 파일 정보
   */
  @NoLogging
  public FileDtl exportToExcel(ExcelExportRequestDTO requestDTO) throws IOException {

    // answerId들로 SpKoAnswer 조회
    List<SpKoAnswer> answers = spKoAnswerRepository.findAllById(requestDTO.getAnswerIds());

    if (answers.isEmpty()) {
      throw new IllegalArgumentException("평가 결과를 찾을 수 없습니다.");
    }

    // Excel 파일 생성
    byte[] excelBytes = generateExcelFile(requestDTO.getUserName(), answers);

    // 파일 저장
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
    String formattedDate = dateFormat.format(new Date());
    String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));

    String originalFileName = "pronunciation_result_" + currentDateTime + ".xlsx";
    String storedFileName = currentDateTime + ".xlsx";

    // 디렉토리 경로 생성 (절대 경로)
    String fullDirPath = UPLOAD_PATH + java.io.File.separator + DIR_NM + java.io.File.separator + formattedDate;
    Path directory = Paths.get(fullDirPath);

    // 디렉토리 생성
    if (!Files.exists(directory)) {
      Files.createDirectories(directory);
    }

    // 파일 저장 경로 (절대 경로)
    String fullFilePath = fullDirPath + java.io.File.separator + storedFileName;

    // 파일 쓰기
    try (FileOutputStream fos = new FileOutputStream(fullFilePath)) {
      fos.write(excelBytes);
    }

    // 상대 경로 (DB 저장용)
    String relativePath = DIR_NM + java.io.File.separator + formattedDate + java.io.File.separator;

    // File 엔티티 생성
    File fileEntity = File.builder()
        .rgtrSysId(99999999999999999L)
        .build();
    fileEntity.create();
    fileRepository.save(fileEntity);

    // FileDtl 엔티티 생성 및 저장
    FileDtl fileDtlEntity = FileDtl.builder()
        .file(fileEntity)
        .fileSn(1)
        .fileStrePath(relativePath)
        .streFileNm(storedFileName)
        .orignlFileNm(originalFileName)
        .fileExtsn("xlsx")
        .fileSize((long) excelBytes.length)
        .delYn(DelYn.N)
        .rgtrSysId(99999999999999999L)
        .build();
    fileDtlEntity.create();
    fileDtlRepository.save(fileDtlEntity);

    return fileDtlEntity;
  }

  /**
   * Excel 파일 생성 (단어 단위 상세)
   *
   * @param userName 사용자 이름
   * @param answers 평가 결과 리스트
   * @return byte[] Excel 파일 바이트
   */
  private byte[] generateExcelFile(String userName, List<SpKoAnswer> answers) throws IOException {

    try (Workbook workbook = new XSSFWorkbook()) {

      // Sheet 1: 요약 정보
      Sheet summarySheet = workbook.createSheet("요약");
      createSummarySheet(workbook, summarySheet, userName, answers);

      // Sheet 2: 상세 결과 (단어별)
      Sheet detailSheet = workbook.createSheet("상세 결과");
      createDetailSheet(workbook, detailSheet, userName, answers);

      // Excel을 바이트 배열로 변환
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      workbook.write(outputStream);
      return outputStream.toByteArray();
    }
  }

  /**
   * 요약 시트 생성
   */
  private void createSummarySheet(Workbook workbook, Sheet sheet, String userName, List<SpKoAnswer> answers) {

    CellStyle headerStyle = createHeaderStyle(workbook);
    CellStyle dataStyle = createDataStyle(workbook);

    int rowNum = 0;

    // 헤더
    Row headerRow = sheet.createRow(rowNum++);
    headerRow.createCell(0).setCellValue("항목");
    headerRow.createCell(1).setCellValue("값");
    headerRow.getCell(0).setCellStyle(headerStyle);
    headerRow.getCell(1).setCellStyle(headerStyle);

    // 사용자 이름
    Row nameRow = sheet.createRow(rowNum++);
    nameRow.createCell(0).setCellValue("이름");
    nameRow.createCell(1).setCellValue(userName);
    nameRow.getCell(0).setCellStyle(dataStyle);
    nameRow.getCell(1).setCellStyle(dataStyle);

    // 생성 일시
    Row dateRow = sheet.createRow(rowNum++);
    dateRow.createCell(0).setCellValue("생성 일시");
    dateRow.createCell(1).setCellValue(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    dateRow.getCell(0).setCellStyle(dataStyle);
    dateRow.getCell(1).setCellStyle(dataStyle);

    // 총 문제 수
    Row countRow = sheet.createRow(rowNum++);
    countRow.createCell(0).setCellValue("총 문제 수");
    countRow.createCell(1).setCellValue(answers.size());
    countRow.getCell(0).setCellStyle(dataStyle);
    countRow.getCell(1).setCellStyle(dataStyle);

    // 평균 점수
    double avgScore = answers.stream()
        .mapToDouble(answer -> {
          try {
            return answer.getResult().get("result").get("quality").get("sentences").get(1).get("score").asDouble();
          } catch (Exception e) {
            return 0.0;
          }
        })
        .average()
        .orElse(0.0);

    Row avgRow = sheet.createRow(rowNum++);
    avgRow.createCell(0).setCellValue("평균 점수");
    avgRow.createCell(1).setCellValue(String.format("%.2f", avgScore));
    avgRow.getCell(0).setCellStyle(dataStyle);
    avgRow.getCell(1).setCellStyle(dataStyle);

    // 열 너비 자동 조정
    sheet.autoSizeColumn(0);
    sheet.autoSizeColumn(1);
  }

  /**
   * 상세 시트 생성 (단어별 행)
   */
  private void createDetailSheet(Workbook workbook, Sheet sheet, String userName, List<SpKoAnswer> answers) {

    CellStyle headerStyle = createHeaderStyle(workbook);
    CellStyle dataStyle = createDataStyle(workbook);

    int rowNum = 0;

    // 헤더 행
    Row headerRow = sheet.createRow(rowNum++);
    String[] headers = {"이름", "문제번호", "문장", "단어", "단어점수", "음소점수"};
    for (int i = 0; i < headers.length; i++) {
      Cell cell = headerRow.createCell(i);
      cell.setCellValue(headers[i]);
      cell.setCellStyle(headerStyle);
    }

    // 데이터 행 (단어별)
    for (SpKoAnswer answer : answers) {
      try {
        JsonNode resultNode = answer.getResult();
        JsonNode sentenceNode = resultNode.get("result").get("quality").get("sentences").get(1);

        String sentence = sentenceNode.get("text").asText();
        int questionNumber = answer.getSpKoQuestion().getOrder();
        JsonNode words = sentenceNode.get("words");

        // 각 단어별로 행 생성
        for (JsonNode word : words) {
          String wordText = word.get("text").asText();

          // !SIL (휴지구간) 제외
          if ("!SIL".equals(wordText)) {
            continue;
          }

          double wordScore = word.get("score").asDouble();

          // 음소 점수 수집
          StringBuilder phonemeScores = new StringBuilder();
          JsonNode syllables = word.get("syll");
          for (JsonNode syllable : syllables) {
            JsonNode phones = syllable.get("phones");
            for (JsonNode phone : phones) {
              String symbol = phone.get("symbol").asText();
              double phoneScore = phone.get("score").asDouble();
              if (phonemeScores.length() > 0) {
                phonemeScores.append(", ");
              }
              phonemeScores.append(symbol).append(":").append(String.format("%.1f", phoneScore));
            }
          }

          // 행 생성
          Row dataRow = sheet.createRow(rowNum++);
          dataRow.createCell(0).setCellValue(userName);
          dataRow.createCell(1).setCellValue(questionNumber);
          dataRow.createCell(2).setCellValue(sentence);
          dataRow.createCell(3).setCellValue(wordText);
          dataRow.createCell(4).setCellValue(String.format("%.2f", wordScore));
          dataRow.createCell(5).setCellValue(phonemeScores.toString());

          // 스타일 적용
          for (int i = 0; i < 6; i++) {
            dataRow.getCell(i).setCellStyle(dataStyle);
          }
        }
      } catch (Exception e) {
        LOGGER.error("Failed to process answer {}: {}", answer.getAnswerId(), e.getMessage());
      }
    }

    // 열 너비 자동 조정
    for (int i = 0; i < headers.length; i++) {
      sheet.autoSizeColumn(i);
      // 한글 폰트 고려하여 너비 추가 조정
      sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1024);
    }
  }

  /**
   * 헤더 스타일 생성
   */
  private CellStyle createHeaderStyle(Workbook workbook) {
    CellStyle style = workbook.createCellStyle();
    Font font = workbook.createFont();
    font.setBold(true);
    font.setFontHeightInPoints((short) 12);
    style.setFont(font);
    style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    style.setBorderTop(BorderStyle.THIN);
    style.setBorderBottom(BorderStyle.THIN);
    style.setBorderLeft(BorderStyle.THIN);
    style.setBorderRight(BorderStyle.THIN);
    style.setAlignment(HorizontalAlignment.CENTER);
    style.setVerticalAlignment(VerticalAlignment.CENTER);
    return style;
  }

  /**
   * 데이터 스타일 생성
   */
  private CellStyle createDataStyle(Workbook workbook) {
    CellStyle style = workbook.createCellStyle();
    style.setBorderTop(BorderStyle.THIN);
    style.setBorderBottom(BorderStyle.THIN);
    style.setBorderLeft(BorderStyle.THIN);
    style.setBorderRight(BorderStyle.THIN);
    style.setVerticalAlignment(VerticalAlignment.CENTER);
    style.setWrapText(true);
    return style;
  }
}
