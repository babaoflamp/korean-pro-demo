package com.mk.api.log.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mk.api.log.domain.SysLog;

public interface SysLogRepository extends JpaRepository<SysLog, Long> {

}
