package org.notnaturalselection.jexcel.parsing;

import java.lang.reflect.Field;
import java.util.function.Supplier;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.notnaturalselection.jexcel.exceptions.AbstractParseException;
import org.notnaturalselection.jexcel.exceptions.FieldMappingException;
import org.notnaturalselection.jexcel.exceptions.MissRequiredFieldException;

public class StandardParser<T, H extends Header> extends AbstractParser<T> {

    private final Supplier<T> supplier;
    private final Iterable<H> headers;

    public StandardParser(
            int verticalOffset, int horizontalOffset, Supplier<T> supplier, Iterable<H> headers
    ) {
        this(WarningPolicy.STRONG, verticalOffset, horizontalOffset, supplier, headers);
    }

    public StandardParser(
            WarningPolicy wp, int verticalOffset, int horizontalOffset, Supplier<T> supplier, Iterable<H> headers
    ) {
        super.warningPolicy = wp;
        super.verticalOffset = verticalOffset;
        super.horizontalOffset = horizontalOffset;
        this.supplier = supplier;
        this.headers = headers;
    }

    public StandardParser(Supplier<T> supplier, Iterable<H> headers) {
        this(0, 0, supplier, headers);
    }

    public StandardParser(WarningPolicy wp, Supplier<T> supplier, Iterable<H> headers) {
        this(wp, 0, 0, supplier, headers);
    }

    @Override
    protected T parseRow(Row row)
            throws AbstractParseException {
        T instance = supplier.get();
        for (H header : headers) {
            Cell cell = row.getCell(header.getColumnNumber() + horizontalOffset);
            parseCell(cell, header, instance);
        }
        return instance;
    }

    private void parseCell(Cell cell, H header, T instance)
            throws AbstractParseException {
        try {
            Field field = instance.getClass().getField(header.getColumnName());
            Object value = FieldType.defineType(field.getType()).getCreator().apply(cell);
            if (value == null) {
                if (header.isRequired()) {
                    handleException(new MissRequiredFieldException("No required field " + header.getColumnName() + " in cell " + cell.getAddress().toString()));
                } else {
                    value = header.getDefaultValue();
                }
            }
            boolean isAccessible = field.isAccessible();
            field.setAccessible(true);
            try {
                field.set(instance, value);
            } catch (IllegalAccessException ignored) {
            }
            field.setAccessible(isAccessible);
        } catch (NoSuchFieldException e) {
            handleException(new FieldMappingException(instance.getClass().toString() + " has no field with name " + header.getColumnName()));
        }
    }
}
