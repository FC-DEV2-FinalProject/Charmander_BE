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

        if(Strings.isBlank(prefix)){
            if(identifier.startsWith("/")) {
                return identifier.substring(1);
            }
            return identifier;
        }

        if(prefix.startsWith("/"))  {
            prefix = prefix.substring(1);
        }

        if(identifier.startsWith("/")) {
            return prefix + identifier;
        }

        return prefix + "/" + identifier;
    }
}
