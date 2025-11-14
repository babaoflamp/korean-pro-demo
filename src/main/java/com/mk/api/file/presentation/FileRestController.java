package com.mk.api.file.presentation;

import java.util.List;
import java.util.Map;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.mk.api.file.application.FileService;
import com.mk.api.file.application.dto.FileDtlCreateDTO;
import com.mk.api.file.application.dto.FileListParam;
import com.mk.api.file.application.dto.FileUpdateDTO;
import com.mk.api.file.domain.FileDtl;
import com.mk.common.ApiResponse;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FileRestController {

  // RestAPI Controller

  private final FileService fileService;

  @GetMapping("/multipart")
  public ApiResponse<Map<String, Object>> getMultipartConfig() {

    return fileService.getMultipartConfig();
  }

  @GetMapping("/files")
  public ApiResponse<List<FileDtl>> findAll(FileListParam param) {

    return fileService.findAll(param);
  }

  @GetMapping("/file/{fileDtlSeq}")
  public ResponseEntity<Resource> findById(
      @PathVariable("fileDtlSeq") @Min(value = 0, message = "요청이 올바르지 않습니다.") Long fileDtlSeq) {

    return fileService.findById(fileDtlSeq);
  }

  @PostMapping("/file")
  public ApiResponse<Long> create(@ModelAttribute FileDtlCreateDTO createDTO,
      MultipartHttpServletRequest multiRequest) {

    return fileService.create(createDTO, multiRequest);
  }

  @PatchMapping("/file")
  public ApiResponse<Long> update(@ModelAttribute FileUpdateDTO updateDTO,
      MultipartHttpServletRequest multiRequest) {

    return fileService.update(updateDTO, multiRequest);
  }

}
