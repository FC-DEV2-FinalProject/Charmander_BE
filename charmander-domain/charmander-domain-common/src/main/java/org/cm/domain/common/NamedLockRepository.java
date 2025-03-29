package org.cm.domain.common;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class NamedLockRepository {

    private final EntityManager entityManager;

    public NamedLockRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public boolean getLock(String lockName) {
        Query query = entityManager.createNativeQuery("SELECT GET_LOCK(?lockName, 2)");
        query.setParameter("lockName", lockName);
        return query.getSingleResult().equals(1);
    }

    @Transactional
    public boolean releaseLock(String lockName) {
        Query query = entityManager.createNativeQuery("SELECT RELEASE_LOCK(?lockName)");
        query.setParameter("lockName", lockName);
        return query.getSingleResult().equals(1);
    }
}
