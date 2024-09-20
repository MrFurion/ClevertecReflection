package by.clevertec.converters;

import by.clevertec.interfaces.ConverterBigDecimal;

import java.math.BigDecimal;

public class ConvertSpecialTypes implements ConverterBigDecimal {
    public String specialTypes(Object value) {
        if (value instanceof BigDecimal) {
            return value.toString();
        }
        return null;
    }
}
