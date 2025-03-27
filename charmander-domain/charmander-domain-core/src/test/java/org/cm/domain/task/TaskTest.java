package org.cm.domain.task;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Duration;
import org.cm.exception.CoreDomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TaskTest {

    @Nested
    @DisplayName("작업 시작")
    class TaskStartTest {

        @Test
        void 작업_시작이_되면_시작_상태가된다() {
            var task = new Task();

            task.start();

            assertThat(task.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
        }

        @Test
        void 작업이_두_번_시작되도_한_번만_변경된다() {
            var task = new Task();

            task.start();
            task.start();

            assertThat(task.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
        }

        @Test
        void 작업_시작을_호출하려면_시작과_진행상태에서만_가능합니다() {
            assertThatThrownBy(() -> {
                var task = new Task();
                task.start();
                task.cancel();
                task.start();
            }).isInstanceOf(CoreDomainException.class);

            assertThatThrownBy(() -> {
                var task = new Task();

                task.start();
                task.succeed();
                task.start();
            }).isInstanceOf(CoreDomainException.class);
        }
    }

    @Nested
    @DisplayName("작업 완료")
    class TaskCompleteTest {

        @Test
        void 작업이_정상적으로_완료된다() {
            var task = new Task();

            task.start();
            task.succeed();

            assertThat(task.getStatus()).isEqualTo(TaskStatus.SUCCESS);
        }

        @Test
        void 작업이_시작되지_않았다면_완료할_수_없습니다() {
            var task = new Task();

            assertThatThrownBy(task::succeed)
                    .isInstanceOf(CoreDomainException.class);
        }

        @Test
        void 작업_완료는_진행상태만_가능합니다() {
            var taskOutput = new TaskOutput("fileId", Duration.ofSeconds(10));
            assertThatThrownBy(() -> {
                var task = new Task();

                task.succeed();
            }).isInstanceOf(CoreDomainException.class);

            assertThatThrownBy(() -> {
                var task = new Task();

                task.start();
                task.cancel();
                task.succeed();
            }).isInstanceOf(CoreDomainException.class);

            assertThatThrownBy(() -> {
                var task = new Task();

                task.start();
                task.succeed();
                task.succeed();
            }).isInstanceOf(CoreDomainException.class);
        }
    }

    @Nested
    @DisplayName("작업 취소")
    class TaskCancelTest {

        @Test
        void 작업이_정상적으로_최소된다() {
            var task = new Task();

            task.start();
            task.cancel();

            assertThat(task.getStatus()).isEqualTo(TaskStatus.CANCELED);
        }

    }
}
