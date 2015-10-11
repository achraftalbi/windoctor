package com.winbit.windoctor.domain.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Created by achraftalbi on 10/11/15.
 */
@Converter
public class BooleanToStringConverter implements AttributeConverter<Boolean, String> {

    @Override
    public String convertToDatabaseColumn(Boolean value) {
        return (value != null && value) ? "Y" : "N";
    }

    @Override
    public Boolean convertToEntityAttribute(String value) {
        return "Y".equals(value);
    }
}
