package org.cm.common.utils;

import java.util.IllegalFormatCodePointException;
import java.util.UUID;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RandomKeyGenerator {

    public String generateRandomKey(int size) {
        if(size <= 0 || size > 32) {
            throw new IllegalArgumentException("랜덤한 키 생성값은 1~32까지 가능합니다.");
        }

        return UUID.randomUUID().toString()
                .replace("-", "")
                .substring(0, size);
    }

    public String generateRandomKey(){
        return generateRandomKey(32);
    }
}
