package com.mk.api.file.infrastructure;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.mk.api.file.domain.FileDtl;

public interface FileDtlRepository extends JpaRepository<FileDtl, Long> {

  // 파일 목록
  List<FileDtl> findAllByFile_fileSeq(Long fileSeq);

  // 파일 순번(fileSn) +1 값 구하기 (기존 쿼리 기반)
  Integer findTopByFile_fileSeqOrderByFileSnDesc(Long fileSeq);

}
