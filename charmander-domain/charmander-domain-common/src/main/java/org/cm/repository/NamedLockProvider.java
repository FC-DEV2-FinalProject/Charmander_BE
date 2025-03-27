package org.cm.repository;

public interface NamedLockProvider {
    void acquire(String lockName);
    void release(String lockName);
}
