package org.cm.infra.storage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ContentsLocatorTest {

    @Test
    @DisplayName("prefix의 앞 부분이 슬래시로 시작하면 제거한다.")
    void Contents_Locator_test_Case_1(){
        ContentsLocator contentsLocator = () -> "/prefix";

        assertThat(contentsLocator.combineLocation("id"))
                .isEqualTo("prefix/id");
    }

    @Test
    @DisplayName("식별 값 첫 문자가 /로 시작하면 제거한다.")
    void Contents_Locator_test_Case_2(){
        ContentsLocator contentsLocator = () -> "prefix";

        assertThat(contentsLocator.combineLocation("/id"))
                .isEqualTo("prefix/id");
    }

    @Test
    @DisplayName("prefix의 앞 부분(/) 을 제거하고 넣는다.")
    void Contents_Locator_test_Case_3(){
        ContentsLocator contentsLocator = () -> "/prefix";

        assertThat(contentsLocator.combineLocation("/id"))
                .isEqualTo("prefix/id");
    }

    @Test
    @DisplayName("prefix와 식별 값을 결합한다")
    void Contents_Locator_test_Case_4(){
        ContentsLocator contentsLocator = () -> "prefix";

        assertThat(contentsLocator.combineLocation("id"))
                .isEqualTo("prefix/id");
    }

    @Test
    @DisplayName("prefix가 없는 경우 식별값만 들어간다.")
    void Contents_Locator_test_Case_5(){
        ContentsLocator contentsLocator = () -> "";

        assertThat(contentsLocator.combineLocation("id"))
                .isEqualTo("id");
    }

    @Test
    @DisplayName("식별값이 없는 경우는 오류가 발생한다. ")
    void Contents_Locator_test_Case_6(){
        ContentsLocator contentsLocator = () -> "";
        assertThatThrownBy(() -> contentsLocator.combineLocation(""))
                .isInstanceOf(Exception.class);
    }

}