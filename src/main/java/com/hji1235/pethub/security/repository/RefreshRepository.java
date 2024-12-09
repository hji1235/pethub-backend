package com.hji1235.pethub.security.repository;

import com.hji1235.pethub.security.entity.Refresh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshRepository extends JpaRepository<Refresh, Long> {

    boolean existsByToken(String refresh);

    @Transactional
    void deleteByToken(String refresh);
}
