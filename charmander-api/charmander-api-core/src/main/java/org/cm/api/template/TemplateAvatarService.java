package org.cm.api.template;

import lombok.RequiredArgsConstructor;
import org.cm.domain.template.TemplateAvatar;
import org.cm.domain.template.TemplateAvatarRepository;
import org.cm.security.AuthInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TemplateAvatarService {
    private final TemplateAvatarRepository templateAvatarRepository;

    public List<TemplateAvatar> getAvatars(AuthInfo authInfo) {
        return templateAvatarRepository.findAllForMember(authInfo.getMemberId());
    }
}
