package org.cm.repository;

import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.dao.OptimisticLockingFailureException;

@Slf4j
public class NamedLockTemplate {
    private NamedLockProvider lockProvider;
    private int retires;
    private int backoffMs;

    NamedLockTemplate(NamedLockProvider lockProvider, int retires, int backoffMs) {
        this.lockProvider = lockProvider;
        this.retires = retires;
        this.backoffMs = backoffMs;
    }

    // TODO: 코드가 드럽다. 개선할 수 잇을 듯?
    // TODO: 메소드 이름 마음에 안듦. run --> executeWithLock로 변경
    public void runWithNamedLockFallback(String lockName, Runnable func) {
        for (int i = 0; i < retires; i++) {
            try {
                func.run();
                return;
            } catch (OptimisticLockingFailureException e) {
                waitForBackoff(backoffMs);
            }
        }
        runWithNamedLock(lockName, func);
    }

    private void acquireLock(@NonNull String lockName) {
        lockProvider.acquire(lockName);
    }

    private void releaseLock(@NonNull String lockName) {
        lockProvider.release(lockName);
    }

    public static NamedLockTemplate.Builder builder() {
        return new Builder();
    }

    private void waitForBackoff(int backoffMs) {
        // TODO: Thread.sleep??
        if (backoffMs <= 0) {
            return;
        }

        try {
            Thread.sleep(backoffMs);
        } catch (InterruptedException e2) {
            Thread.currentThread().interrupt();
            return;
        } catch (Exception e3) {
            throw new RuntimeException(e3);
        }
    }

    private void runWithNamedLock(String lockName, Runnable func) {
        try {
            acquireLock(lockName);
            func.run();
        } catch (Exception e) {
            // TODO
            throw new RuntimeException(e);
        } finally {
            releaseLock(lockName);
        }
    }
    @Setter
    @Accessors(chain = true)
    public static class Builder {
        private NamedLockProvider lockProvider;

        private int retry = 5;
        private int backoffMs = 200;

        public NamedLockTemplate build() {
            validate();
            return new NamedLockTemplate(lockProvider, retry, backoffMs);
        }

        private void validate() {
            if (lockProvider == null) {
                throw new IllegalArgumentException("LockProvider can not be null");
            }
            if (retry <= 0) {
                log.warn("retry가 0보다 작아서 바로 NamedLock을 획득하고 실행함.");
            }
            if (backoffMs <= 0) {
                log.warn("backoffMs가 0보다 작아서 작업 실패 후 대기 시간을 가지지 않음");
            }
        }
    }
}
