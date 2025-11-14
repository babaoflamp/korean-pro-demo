package com.mk.api.engine.presentation.sp;

import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mk.api.engine.application.SpDemoService;
import com.mk.api.engine.application.dto.sp.ko.SpKoEvaluateDTO;
import com.mk.api.engine.application.dto.sp.ko.SpKoModelDTO;
import com.mk.api.engine.application.dto.sp.ko.SpKoQuestionDTO;
import com.mk.api.file.application.FileService;
import com.mk.api.file.application.dto.FileListParam;
import com.mk.api.file.domain.FileDtl;
import com.mk.common.ApiResponse;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/sp/demo")
@RequiredArgsConstructor
public class SpKoDemoRestController {

  private final SpDemoService spDemoService;
  private final FileService fileService;

  @GetMapping("/question")
  public ApiResponse<List<SpKoQuestionDTO>> findAll() {

    return spDemoService.findAll();
  }

  @GetMapping("/question/{id}")
  public ApiResponse<SpKoQuestionDTO> findById(@PathVariable("id") String id) {

    return spDemoService.findById(id);
  }

  // @GetMapping("/answer/{id}/pre")
  // public ApiResponse<SpKoAnswer> findPrevious(@PathVariable("id") String id) {
  //
  // return spDemoService.findPrevious(id);
  // }

  // engine
  @PostMapping("/model")
  public ApiResponse<SpKoModelDTO> createModel(@RequestBody SpKoModelDTO dto) {

    return spDemoService.createModel(dto);
  }

  @PostMapping("/evaluate")
  public ApiResponse<?> createEvalueate(@RequestBody SpKoEvaluateDTO dto) {

    return spDemoService.createEvaluate(dto);
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



}
