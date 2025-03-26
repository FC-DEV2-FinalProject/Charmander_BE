package org.cm.domain.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface NamedLockRepository extends JpaRepository {

    // 네임드 get락
    @Transactional
    @Query(value = "SELECT GET_LOCK(:lockName, 2)", nativeQuery = true)
    boolean getLock(@Param("lockName") String lockName);

    // 네임드 release락
    @Transactional
    @Query(value = "SELECT RELEASE_LOCK(:lockName)", nativeQuery = true)
    Integer releaseLock(@Param("lockName") String lockName);
}
