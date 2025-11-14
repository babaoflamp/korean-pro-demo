package com.mk.api.engine.infrastructure.sp.ko;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mk.api.engine.domain.sp.ko.SpKoQuestion;

public interface SpKoQuestionRepository
    extends JpaRepository<SpKoQuestion, Long>, SpKoQuestionRepositoryCustom {

}
