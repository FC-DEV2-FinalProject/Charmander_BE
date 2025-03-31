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
    TemplateAvatarService.class
})
@DisplayName("[통합 테스트] TemplateAvatarService")
class TemplateAvatarServiceIntegrationTest extends BaseServiceIntergrationTest {
    @Autowired
    TemplateAvatarService templateAvatarService;

    @Nested
    @DisplayName("[조회]")
    class ReadTest {
        @Test
        @DisplayName("001. 모든 사용자에게 공유된 아바타를 조회할 수 있다.")
        void 모든_사용자에게_공유된_아바타를_조회할_수_있다() {
            // given
            var member = createMember();
            var avatars = List.of(
                createTemplateAvatar(null)
            );

            var authInfo = new AuthInfo(member.getId());

            // when
            var items = templateAvatarService.getAvatars(authInfo);

            // then
            assertThat(items).hasSize(avatars.size());
        }

        @Test
        @DisplayName("002. 사용자가 소유한 아바타 목록을 조회할 수 있다.")
        void 사용자가_소유한_아바타_목록을_조회할_수_있다() {
            // given
            var member = createMember();
            var avatars = List.of(
                createTemplateAvatar(member)
            );

            var authInfo = new AuthInfo(member.getId());

            // when
            var items = templateAvatarService.getAvatars(authInfo);

            // then
            assertThat(items).hasSize(avatars.size());
        }

        @Test
        @DisplayName("003. 사용자가 소유한 아바타와 모든 사용자에게 공유된 아바타를 동시에 조회할 수 있어야 한다.")
        void 사용자가_소유한_아바타와_모든_사용자에게_공유된_아바타를_동시에_조회할_수_있어야_한다() {
            // given
            var member = createMember();
            var avatars = List.of(
                createTemplateAvatar(member),
                createTemplateAvatar(null)
            );

            var authInfo = new AuthInfo(member.getId());

            // when
            var items = templateAvatarService.getAvatars(authInfo);

            // then
            assertThat(items).hasSize(avatars.size());
        }

        @Test
        @DisplayName("004. 사용자가 소유하지 않은 아바타를 조회할 수 없어야 한다.")
        void 사용자가_소유하지_않은_아바타를_조회할_수_없어야_한다() {
            // given
            var member1 = createMember();
            var member2 = createMember();
            var avatars = List.of(
                createTemplateAvatar(member2)
            );

            var authInfo = new AuthInfo(member1.getId());

            // when
            var items = templateAvatarService.getAvatars(authInfo);

            // then
            assertThat(items).hasSize(0);
        }
    }
}
