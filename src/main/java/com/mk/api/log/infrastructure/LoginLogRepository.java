package com.mk.api.log.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mk.api.log.domain.LoginLog;

public interface LoginLogRepository extends JpaRepository<LoginLog, Long> {

}
