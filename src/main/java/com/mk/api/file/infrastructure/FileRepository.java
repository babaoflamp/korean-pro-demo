package com.mk.api.file.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mk.api.file.domain.File;

public interface FileRepository extends JpaRepository<File, Long> {

}
