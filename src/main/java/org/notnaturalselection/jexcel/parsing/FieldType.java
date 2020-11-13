package org.notnaturalselection.jexcel.parsing;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.function.Function;

import org.apache.poi.ss.usermodel.Cell;

import org.notnaturalselection.jexcel.exceptions.IllegalFieldTypeException;

public enum FieldType {
    BYTE(Byte.class, (cell ->new Double(cell.getNumericCellValue()).byteValue())),
    SHORT(Short.class, (cell -> new Double(cell.getNumericCellValue()).shortValue())),
    INTEGER(Integer.class, (cell -> new Double(cell.getNumericCellValue()).intValue())),
    LONG(Long.class, (cell -> new Double(cell.getNumericCellValue()).longValue())),
    FLOAT(Float.class, (cell -> new Double(cell.getNumericCellValue()).floatValue())),
    DOUBLE(Double.class, (Cell::getNumericCellValue)),
    DATE(Date.class, (Cell::getDateCellValue)),
    LOCAL_DATE_TIME(LocalDateTime.class, (cell -> cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())),
    LOCAL_DATE(LocalDate.class, (cell -> cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())),
    ZONED_DATE_TIME(ZonedDateTime.class, (cell -> cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()))),
    OFFSET_DATE_TIME(OffsetDateTime.class, (cell -> cell.getDateCellValue().toInstant().atOffset(ZoneOffset.of(ZoneOffset.systemDefault().getId())).toOffsetTime())),
    OFFSET_TIME(OffsetTime.class, (cell -> cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toOffsetDateTime().toOffsetTime())),
    LOCAL_TIME(LocalTime.class, (cell -> cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalTime())),
    STRING(String.class, (Cell::getStringCellValue)),
    BOOLEAN(Boolean.class, (Cell::getBooleanCellValue));

    private final Class<?> aClass;

    private final Function<Cell, Object> creator;

    FieldType(Class<?> aClass, Function<Cell, Object> creator) {
        this.aClass = aClass;
        this.creator = creator;
    }

    public Class<?> getAClass() {
        return aClass;
    }

    public Function<Cell, Object> getCreator() {
        return creator;
    }

    public static FieldType defineType(Class<?> aClass)
            throws IllegalFieldTypeException {
        return Arrays
                .stream(FieldType.values())
                .filter(it -> it.getAClass() == aClass)
                .findFirst()
                .orElseThrow(() -> new IllegalFieldTypeException("No type for class " + aClass.getName()));
    }
}
