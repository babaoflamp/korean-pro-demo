package com.mk.api.file.infrastructure;

import java.util.List;
import com.mk.api.file.application.dto.FileDTO;
import com.mk.api.file.application.dto.FileDetailDTO;
import com.mk.api.file.application.dto.FileListParam;
import com.mk.api.file.application.dto.FileUpdateDTO;
import com.mk.api.file.domain.File;
import com.mk.api.file.domain.FileDtl;

public interface FileMapper {

  public Integer countAll(FileListParam param);

  public List<FileDTO> findAll(FileListParam param);

  public FileDetailDTO findById(Long fileDtlSeq);

  public int findMaxFileSn(FileListParam param);

  public Long create(File file);

  public Long createFileDtlMulti(List<FileDtl> list);

  public Long deleteFileDtlMulti(FileUpdateDTO file);
}
