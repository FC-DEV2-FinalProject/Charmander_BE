package org.cm.infra.storage;

public class AwsContentsLocator implements ContentsLocator {
    private final String bucket;
    private final String prefix;

    public AwsContentsLocator(String bucket, String prefix) {
        this.bucket = bucket;
        this.prefix = prefix;
    }

    @Override
    public String prefix() {
        return bucket + prefix;
    }
}
