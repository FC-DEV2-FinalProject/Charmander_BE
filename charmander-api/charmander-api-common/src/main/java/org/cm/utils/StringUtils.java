package org.cm.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtils {
    private static final CharSequence chars = "0123456789";
    private static final CharSequence strongChars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!@#$%^&*()_+-=[]{}|;:,.<>?";

    public static String generateRandomNumberString(int length) {
        return generate(chars, length);
    }

    public static String generateRandomStrongString(int length) {
        return generate(strongChars, length);
    }

    @SuppressWarnings("SameParameterValue")
    private static String generate(CharSequence chars, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            var idx = (int) (Math.random() * chars.length());
            var chr = chars.charAt(idx);
            sb.append(chr);
        }
        return sb.toString();
    }
}
