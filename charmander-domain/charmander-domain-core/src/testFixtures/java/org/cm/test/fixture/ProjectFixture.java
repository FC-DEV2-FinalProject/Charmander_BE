package org.cm.test.fixture;

import org.cm.domain.member.Member;
import org.cm.domain.project.Project;

public class ProjectFixture {

    public static Project create() {
        return create(MemberFixture.create());
    }

    public static Project create(Member member) {
        return new Project(member, "Project1", 1);
    }
}
