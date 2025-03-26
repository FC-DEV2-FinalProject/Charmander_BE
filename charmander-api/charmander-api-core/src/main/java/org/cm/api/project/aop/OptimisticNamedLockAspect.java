package org.cm.api.project.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.cm.domain.common.NamedLockRepository;
import org.cm.domain.project.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Aspect
@Component
@RequiredArgsConstructor
public class OptimisticNamedLockAspect {

    final private NamedLockRepository namedLockRepository;
    final private ProjectRepository projectRepository;

    private static final Logger logger = LoggerFactory.getLogger(OptimisticNamedLockAspect.class);

    private static final int MAX_RETRIES = 5;

    @Around("@annotation(org.springframework.transaction.annotation.Transactional) && execution(* org.cm.api.project.ProjectService.modifyProjectNewsArticle(..))")
    public Object handleOptimisticLockFailure(ProceedingJoinPoint joinPoint) throws Throwable {
        int retryCount = 0;
        Object[] args = joinPoint.getArgs();
        Long id = (Long) args[0]; // id 파라미터 추출
        Long memberId = (Long) args[1];
        String newArticle = (String) args[2];
        LocalDateTime timestamp = (LocalDateTime) args[3];

        while (retryCount < MAX_RETRIES) {
            try {
                // 비즈니스 로직 실행
                return joinPoint.proceed(); // modifyProjectNewsArticle 메서드를 실행
            } catch (OptimisticLockingFailureException e) {
                retryCount++;
                logger.warn("데이터 충돌 발생: {}회", retryCount);

                // 재시도 전 잠시 대기
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }

                if (retryCount >= MAX_RETRIES) {
                    logger.info("동시성 5회 이상 충돌 발생, 네임드 락 시작");

                    // 네임드 락을 획득
                    String lockName = "project_" + id + "_" + UUID.randomUUID().toString();
                    boolean lockAcquired = namedLockRepository.getLock(lockName);

                    if (!lockAcquired) {
                        logger.error("네임드 락 획득 실패: {}", id);
                        throw new OptimisticLockingFailureException("네임드 락을 획득할 수 없습니다. 잠시 후 다시 시도해주세요.");
                    }

                    try {
                        // 네임드 락을 사용해 원래 메서드 실행
                        projectRepository.updateProjectNewsArticleFindById(id, newArticle, timestamp);
                    } catch (Exception ex) {
                        logger.error("네임드 락 기반 업데이트 실패: {}", ex.getMessage(), ex);
                        throw new RuntimeException("네임드 락을 활용한 업데이트 중 오류가 발생했습니다.", ex);
                    } finally {
                        namedLockRepository.releaseLock(lockName);
                        logger.info("네임드 락 종료");
                    }

                    break; // 네임드 락 후 재시도 하지 않고 종료
                }
            } catch (Exception e) {
                logger.error("예기치 못한 오류 발생: {}", e.getMessage(), e);
                throw new RuntimeException("예기치 못한 오류로 인해 업데이트에 실패했습니다.", e);
            }
        }

        throw new RuntimeException("최대 재시도 횟수를 초과하여 작업을 완료할 수 없습니다.");
    }
}
