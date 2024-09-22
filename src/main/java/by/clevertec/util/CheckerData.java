package by.clevertec.util;

import by.clevertec.interfaces.Checker;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public class CheckerData implements Checker {
    public boolean isPrimitiveOrWrapper(Class<?> clazz) {
        return clazz.isPrimitive() ||
                clazz == String.class ||
                clazz == Integer.class ||
                clazz == Double.class ||
                clazz == Boolean.class ||
                clazz == UUID.class ||
                clazz == LocalDate.class ||
                clazz == OffsetDateTime.class;
    }
}
