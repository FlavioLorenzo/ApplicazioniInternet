package com.internet_application.backend.Enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class ShiftStatusConverter implements AttributeConverter<ShiftStatus, Integer> {
    @Override
    public Integer convertToDatabaseColumn(ShiftStatus status) {
        if (status == null) {
            return null;
        }
        return status.getCode();
    }

    @Override
    public ShiftStatus convertToEntityAttribute(Integer code) {
        return Stream.of(ShiftStatus.values())
                .filter(s -> s.getCode() == code.intValue())
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}