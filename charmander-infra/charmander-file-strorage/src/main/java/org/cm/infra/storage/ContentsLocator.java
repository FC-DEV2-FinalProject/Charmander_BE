package org.cm.infra.storage;

import org.apache.logging.log4j.util.Strings;

@FunctionalInterface
public interface ContentsLocator {

    String prefix();

    default String combineLocation(String identifier) {
        if(Strings.isBlank(identifier)){
            throw new IllegalArgumentException();
        }

        var prefix = prefix();
        if (Strings.isBlank(prefix)) {
            return identifier.startsWith("/") ? identifier.substring(1) : identifier;
        }

        prefix = prefix.startsWith("/") ? prefix.substring(1) : prefix;
        return prefix + (identifier.startsWith("/") ? "" : "/") + identifier;
    }
}
