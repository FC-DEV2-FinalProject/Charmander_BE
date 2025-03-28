package org.cm.test.fixture;

import org.cm.domain.common.ScreenSize;
import org.cm.domain.template.*;

public class TemplateFixture {

    public static Template create() {
        var data = new TemplateData(
            TemplateCategory.Culture,
            "TemplateName",
            new ScreenSize(100, 100),
            createBackground(),
            createAvatar()
        );
        return new Template(data);
    }

    public static TemplateBackground createBackground() {
        return new TemplateBackground(
            "BackgroundName",
            TemplateBackgroundType.Image,
            "fileUrl",
            new ScreenSize(100, 100)
        );
    }

    public static TemplateAvatar createAvatar() {
        return new TemplateAvatar(
            "AvatarName",
            "fileUrl"
        );
    }
}
