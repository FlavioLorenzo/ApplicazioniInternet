package com.internet_application.backend.Enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class RideBookingStatusConverter implements AttributeConverter<RideBookingStatus, Integer> {
    @Override
    public Integer convertToDatabaseColumn(RideBookingStatus status) {
        if (status == null) {
            return null;
        }
        return status.getCode();
    }

    @Override
    public RideBookingStatus convertToEntityAttribute(Integer code) {
        return Stream.of(RideBookingStatus.values())
                .filter(s -> s.getCode() == code.intValue())
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}