package com.mk.api.engine.application.dto.sp.ko;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExcelExportRequestDTO {

  private String userName;
  private List<Long> answerIds;

}
