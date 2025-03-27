package org.cm.repository;

import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.dao.OptimisticLockingFailureException;

@Slf4j
public class NamedLockTemplate {
    NamedLockProvider lockProvider;
    int retires;
    int backoffMs;

    NamedLockTemplate(NamedLockProvider lockProvider, int retires, int backoffMs) {
        this.lockProvider = lockProvider;
        this.retires = retires;
        this.backoffMs = backoffMs;
    }

    // TODO: 코드가 드럽다. 개선할 수 잇을 듯?
    // TODO: 메소드 이름 마음에 안듦. run --> executeWithLock로 변경
    public void executeWithLock(String lockName, Runnable func) {
        for (int i = 0; i < retires; i++) {
            try {
                func.run();
                return;
            } catch (OptimisticLockingFailureException e) {
                // TODO: Thread.sleep??
                if (backoffMs <= 0) {
                    continue;
                }

                try {
                    Thread.sleep(backoffMs);
                } catch (InterruptedException e2) {
                    break;
                } catch (Exception e3) {
                    throw new RuntimeException(e3);
                }
            }
        }

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

    private void acquireLock(@NonNull String lockName) {
        lockProvider.acquire(lockName);
    }

    private void releaseLock(@NonNull String lockName) {
        lockProvider.release(lockName);
    }

    public static NamedLockTemplate.Builder builder() {
        return new Builder();
    }

    @Setter
    @Accessors(chain = true)
    public static class Builder {
        NamedLockProvider lockProvider;

        int retry = 5;
        int backoffMs = 200;

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
