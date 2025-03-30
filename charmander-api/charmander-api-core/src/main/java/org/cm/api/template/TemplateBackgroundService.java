package org.cm.api.template;

import lombok.RequiredArgsConstructor;
import org.cm.domain.template.TemplateBackground;
import org.cm.domain.template.TemplateBackgroundRepository;
import org.cm.security.AuthInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TemplateBackgroundService {
    private final TemplateBackgroundRepository templateBackgroundRepository;

    public List<TemplateBackground> getBackgrounds(AuthInfo authInfo) {
        return templateBackgroundRepository.findAllForMember(authInfo.getMemberId());
    }
}
