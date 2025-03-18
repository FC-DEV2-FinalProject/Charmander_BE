package org.cm.fixture;

import org.cm.domain.member.Member;
import org.cm.domain.project.Project;
import org.cm.domain.project.ProjectStatus;

public class ProjectFixture {

    public static Project create(Member member){
        return new Project(member, "Project1", ProjectStatus.DRAFT, "", 1);
    }
}
