package org.cm.domain.common;

import jakarta.persistence.AttributeConverter;

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
        throw new IllegalArgumentException("Unknown value: " + dbData);
    }
}
