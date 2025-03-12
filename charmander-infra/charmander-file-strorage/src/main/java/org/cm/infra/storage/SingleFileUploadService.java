package org.cm.infra.storage;

import org.cm.common.domain.File;
import org.springframework.web.multipart.MultipartFile;

public interface SingleFileUploadService {
    File upload(MultipartFile file, ContentsLocator locator);
}
