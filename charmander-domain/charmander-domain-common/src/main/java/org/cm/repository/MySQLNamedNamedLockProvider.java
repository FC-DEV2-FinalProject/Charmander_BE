package org.cm.repository;

import lombok.RequiredArgsConstructor;
import org.cm.domain.common.NamedLockRepository;

@RequiredArgsConstructor
public class MySQLNamedNamedLockProvider implements NamedLockProvider {
    private final NamedLockRepository repository;

    @Override
    public void acquire(String lockName) {
        repository.getLock(lockName);
    }

    @Override
    public void release(String lockName) {
        repository.releaseLock(lockName);
    }
}
