package org.cm.domain.common;

import jakarta.persistence.AttributeConverter;
import lombok.extern.slf4j.Slf4j;
import org.cm.domain.exception.CommonDomainException;
import org.cm.domain.exception.CommonDomainExceptionCode;

@Slf4j
@SuppressWarnings("ConverterNotAnnotatedInspection")
public abstract class GenericEnumConverter<T extends Enum<T> & PersistenceEnum<E>, E> implements
    AttributeConverter<T, E> {
    private final Class<T> enumClass;

    public GenericEnumConverter(Class<T> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public E convertToDatabaseColumn(T attribute) {
        return attribute.getValue();
    }

    @Override
    public T convertToEntityAttribute(E dbData) {
        T[] enums = enumClass.getEnumConstants();
        for (T e : enums) {
            if (e.getValue().equals(dbData)) {
                return e;
            }
        }
        log.error("unknown value: {}", dbData);
        throw new CommonDomainException(CommonDomainExceptionCode.ELEMENT_NOT_FOUND);
    }
}
