package org.cm.test.fixture;

import org.cm.domain.file.UploadedFile;
import org.cm.domain.member.Member;

public class UploadedFileFixture {
    public static UploadedFile create(String uploadId, String fileId, Member member) {
        return UploadedFile.createUserUploadFile(uploadId, fileId, member.getId());
    }
}
