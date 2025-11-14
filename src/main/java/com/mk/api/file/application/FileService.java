package com.mk.api.file.application;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.mk.api.file.application.dto.FileDTO;
import com.mk.api.file.application.dto.FileDtlCreateDTO;
import com.mk.api.file.application.dto.FileListParam;
import com.mk.api.file.application.dto.FileUpdateDTO;
import com.mk.api.file.domain.File;
import com.mk.api.file.domain.FileDtl;
import com.mk.api.file.infrastructure.FileDtlRepository;
import com.mk.api.file.infrastructure.FileRepository;
import com.mk.api.file.value.DelYn;
import com.mk.common.ApiResponse;
import com.mk.common.FileUtil;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class FileService {

  private static final Logger LOGGER = LoggerFactory.getLogger(FileService.class);

  private final FileRepository fileRepository;

  private final FileDtlRepository fileDtlRespository;

  private final FileUtil fileUtil;

  private final static String EVAL_DIR = "EVAL";

  @Value("${spring.servlet.multipart.max-request-size}")
  private String MAX_REQUEST_SIZE;

  @Value("${spring.servlet.multipart.max-file-size}")
  private String MAX_FILE_SIZE;

  // 단위 변환을 위한 상수 정의
  private static final long KB = 1024L;
  private static final long MB = 1024L * KB;
  private static final long GB = 1024L * MB;

  /**
   * multipart config 파일 사이즈 취득
   *
   * @return ApiResponse<Map<String, Object>>
   */
  public ApiResponse<Map<String, Object>> getMultipartConfig() {
    Map<String, Object> map = new HashMap<String, Object>();

    map.put("maxRequestSize", convertToBytes(MAX_REQUEST_SIZE));
    map.put("maxFileSize", convertToBytes(MAX_FILE_SIZE));

    return ApiResponse.of(HttpStatus.OK, map);
  }

  /**
   * byte 변환
   *
   * @param sizeStr
   * @return long
   */
  public long convertToBytes(String sizeStr) {
    if (sizeStr == null || sizeStr.isBlank()) {
      return 0;
    }

    sizeStr = sizeStr.toUpperCase().trim();

    long size = Long.parseLong(sizeStr.replaceAll("[^\\d]", ""));
    String unit = sizeStr.replaceAll("[\\d]", "");

    switch (unit) {
      case "GB":
        return size * GB;
      case "MB":
        return size * MB;
      case "KB":
        return size * KB;
      default:
        return size;
    }
  }

  /**
   * 파일 리스트 조회
   *
   * @param param
   * @return ApiResponse<List<FileDTO>>
   */
  public ApiResponse<List<FileDtl>> findAll(FileListParam param) {

    if (param == null || param.getFileSeq() == null) {
      return ApiResponse.of(HttpStatus.BAD_REQUEST, null);
    }

    try {

      List<FileDtl> list = Optional
          .ofNullable(fileDtlRespository.findAllByFile_fileSeq(param.getFileSeq()))
          .orElseGet(Collections::emptyList)
          .stream()
          .collect(Collectors.toList());

      return list.isEmpty() ? ApiResponse.of(HttpStatus.NOT_FOUND, null)
          : ApiResponse.of(HttpStatus.OK, list);

    } catch (Exception e) {
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

  }

  /**
   * 파일 상세 조회(파일 다운로드)
   *
   * @param fileDtlSeq
   * @return ResponseEntity<Resource>
   */
  public ResponseEntity<Resource> findById(Long fileDtlSeq) {

    try {

      Optional<FileDtl> fileDtl =
          Optional.ofNullable(fileDtlRespository.findById(fileDtlSeq)).orElseGet(null);

      FileDTO info = FileDTO.of(fileDtl.get());


      if (info != null) {
        return fileUtil
            .downloadFile(info.getFileStrePath(), info.getStreFileNm(), info.getOrignlFileNm());
      }

    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.notFound().build();
  }

  /**
   * 파일 등록
   *
   * @param fileParam
   * @param multiRequest
   * @return ApiResponse<Long>
   */
  @Transactional
  public ApiResponse<Long> create(FileDtlCreateDTO createDTO,
      MultipartHttpServletRequest multiRequest) {

    try {

      final Map<String, MultipartFile> files = multiRequest.getFileMap();


      if (files != null && !files.isEmpty()) {

        File file = File.builder().rgtrSysId(null).build();
        file.create();

        fileRepository.save(file);

        // 파일 업로드
        List<FileDtlCreateDTO> list = fileUtil.uploadFile(createDTO, files);
        List<FileDtl> fileDtls = list.stream().map(fileCreateDTO -> {
          FileDtl dto = fileCreateDTO.toEntity(file);
          dto.create();
          return dto;
        }).collect(Collectors.toList());

        // 파일 상세 등록
        if (!list.isEmpty()) {
          fileDtlRespository.saveAll(fileDtls);

          return fileDtls.size() > 0 ? ApiResponse.of(HttpStatus.CREATED, file.getFileSeq())
              : ApiResponse.of(HttpStatus.UNPROCESSABLE_ENTITY, null);
        }
      }

    } catch (IOException e) {
      LOGGER.error("[fileService.insertFile] IOException: " + e.getMessage());
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    } catch (Exception e) {
      LOGGER.error("[fileService.insertFile] Exception : " + e.getMessage());
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

    return ApiResponse.of(HttpStatus.OK, null);
  }

  /**
   * 첨부파일 수정
   *
   * @param updateDTO
   * @param multiRequest
   * @return ApiResponse<Long>
   */
  @Transactional
  public ApiResponse<Long> update(FileUpdateDTO updateDTO,
      MultipartHttpServletRequest multiRequest) {

    if (updateDTO == null || updateDTO.getFileSeq() == null) {
      return ApiResponse.of(HttpStatus.BAD_REQUEST, null);
    }

    try {

      final Map<String, MultipartFile> files = multiRequest.getFileMap();

      updateDTO.setMdfrSysId(null);
      updateDTO.delete();

      // 파일 상세 멀티 삭제
      // fileMapper.deleteFileDtlMulti(updateDTO);

      Optional<File> file = fileRepository.findById(updateDTO.getFileSeq());
      // 확인후 수정
      updateDTO.toEntity(file.get());
      fileRepository.save(file.get());

      // 파일 순서 조회 param
      FileListParam param =
          FileListParam.builder().delYn(DelYn.N).fileSeq(updateDTO.getFileSeq()).build();

      // 파일 등록
      if (files != null && !files.isEmpty()) {

        Optional<Integer> maxSn = Optional
            .ofNullable(
                fileDtlRespository.findTopByFile_fileSeqOrderByFileSnDesc(param.getFileSeq()));

        if (!maxSn.isPresent()) {
          return ApiResponse.of(HttpStatus.NOT_FOUND, null);
        }

        FileDtlCreateDTO createDTO = FileDtlCreateDTO
            .builder()
            .fileSeq(updateDTO.getFileSeq())
            .fileSn(maxSn.get())
            .dirNm(updateDTO.getDirNm())
            .rgtrSysId(Long.valueOf(0L))
            .mdfrSysId(Long.valueOf(0L))
            .build();

        // 파일 업로드
        List<FileDtlCreateDTO> list = fileUtil.uploadFile(createDTO, files);

        // 파일 상세 등록
        if (!list.isEmpty()) {
          // fileMapper.createFileDtlMulti(list);
        } else {
          return ApiResponse.of(HttpStatus.UNPROCESSABLE_ENTITY, null);
        }
      }

      // 파일 수 확인
      Integer result = findAll(param).getData().size();

      return (result > 0) ? ApiResponse.of(HttpStatus.OK, updateDTO.getFileSeq())
          : ApiResponse.of(HttpStatus.NO_CONTENT, null);

    } catch (IOException e) {
      LOGGER.error("[fileService.insertFile] IOException: " + e.getMessage());
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    } catch (Exception e) {
      LOGGER.error("[fileService.insertFile] Exception : " + e.getMessage());
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
  }
}
