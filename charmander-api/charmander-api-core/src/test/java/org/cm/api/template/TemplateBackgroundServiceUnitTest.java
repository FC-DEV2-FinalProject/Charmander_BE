package org.cm.api.template;

import org.cm.security.AuthInfo;
import org.cm.test.config.BaseServiceIntergrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import({
    TemplateBackgroundService.class
})
@DisplayName("[통합 테스트} TemplateBackgroundService")
class TemplateBackgroundServiceIntergrationTest extends BaseServiceIntergrationTest {
    @Autowired
    TemplateBackgroundService templateBackgroundService;

    @Nested
    @DisplayName("[조회]")
    class ReadTest {
        @Test
        @DisplayName("001. 모든 사용자에게 공유된 배경을 조회할 수 있다.")
        void 모든_사용자에게_공유된_배경을_조회할_수_있다() {
            // given
            var member = createMember();
            var backgrounds = List.of(
                createTemplateBackground(null)
            );

            var authInfo = new AuthInfo(member.getId());

            // when
            var items = templateBackgroundService.getBackgrounds(authInfo);

            // then
            assertThat(items).hasSize(backgrounds.size());
        }

        @Test
        @DisplayName("002. 사용자가 소유한 배경 목록 조회할 수 있다.")
        void 사용자가_소유한_배경_목록을_조회할_수_있다() {
            // given
            var member = createMember();
            var backgrounds = List.of(
                createTemplateBackground(member)
            );

            var authInfo = new AuthInfo(member.getId());

            // when
            var items = templateBackgroundService.getBackgrounds(authInfo);

            // then
            assertThat(items).hasSize(backgrounds.size());
        }

        @Test
        @DisplayName("003. 사용자가 소유한 배경과 모든 사용장에게 공유된 배경을 동시에 조회할 수 있어야 한다.")
        void 사용자가_소유한_배경과_모든_사용자에게_공유된_배경을_동시에_조회할_수_있어야_한다() {
            // given
            var member = createMember();
            var backgrounds = List.of(
                createTemplateBackground(member),
                createTemplateBackground(null)
            );

            var authInfo = new AuthInfo(member.getId());

            // when
            var items = templateBackgroundService.getBackgrounds(authInfo);

            // then
            assertThat(items).hasSize(backgrounds.size());
        }

        @Test
        @DisplayName("004. 사용자가 소유하지 않은 배경을 조회할 수 없어야 한다.")
        void 사용자가_소유하지_않은_배경을_조회할_수_없어야_한다() {
            // given
            var member1 = createMember();
            var member2 = createMember();
            var backgrounds = List.of(
                createTemplateBackground(member2)
            );

            var authInfo = new AuthInfo(member1.getId());

            // when
            var items = templateBackgroundService.getBackgrounds(authInfo);

            // then
            assertThat(items).hasSize(0);
        }
    }
}
