package org.notnaturalselection.jexcel.parsing.commons;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.notnaturalselection.jexcel.exceptions.AbstractParseException;
import org.notnaturalselection.jexcel.exceptions.FieldMappingException;
import org.notnaturalselection.jexcel.exceptions.MissRequiredFieldException;
import org.notnaturalselection.jexcel.parsing.dto.ParseResult;

public class ReflectionParser<T> extends AbstractParser<T> {

    private Supplier<T> supplier;

    private Map<Header, Field> fieldMapping;

    public Map<Header, Field> getFieldMapping() {
        return fieldMapping;
    }

    public void setFieldMapping(Map<Header, Field> fieldMapping) {
        this.fieldMapping = fieldMapping;
    }

    public Supplier<T> getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier<T> supplier) {
        this.supplier = supplier;
    }


    public abstract static class Builder<B extends Builder<B, T>, T> {

        protected int verticalOffset = 0;

        protected int horizontalOffset = 0;

        protected WarningPolicy warningPolicy = WarningPolicy.STRONG;

        protected Supplier<T> supplier;

        public B withVerticalOffset(int verticalOffset) {
            this.verticalOffset = verticalOffset;
            return self();
        }

        public B withHorizontalOffset(int horizontalOffset) {
            this.horizontalOffset = horizontalOffset;
            return self();
        }

        public B withWarningPolicy(WarningPolicy wp) {
            this.warningPolicy = wp;
            return self();
        }

        public B withSupplier(Supplier<T> supplier) {
            this.supplier = supplier;
            return self();
        }

        public abstract AbstractParser<T> build()
                throws AbstractParseException;

        protected abstract B self();
    }

    public ParseResult<T> parseWorkbook(Workbook workbook)
            throws AbstractParseException {
        if (getFieldMapping() == null || getFieldMapping().isEmpty()) {
            throw new FieldMappingException("No field mapping is set");
        }
        return super.parseWorkbook(workbook);
    }

    public ReflectionParser(int verticalOffset, int horizontalOffset, WarningPolicy warningPolicy, Supplier<T> supplier) {
        super(verticalOffset, horizontalOffset, warningPolicy);
        this.supplier = supplier;
    }

    protected T parseRow(Row row, List<String> warnings)
            throws AbstractParseException {
        T instance = getSupplier().get();
        for (Map.Entry<Header, Field> entry : getFieldMapping().entrySet()) {
            Cell cell = row.getCell(entry.getKey().getColumnNumber() + getHorizontalOffset());
            parseCell(cell, entry.getKey(), entry.getValue(), instance, warnings);
        }
        return instance;
    }

    protected void parseCell(Cell cell, Header header, Field field, T instance, List<String> warnings)
            throws AbstractParseException {
        Object value = getValue(cell, header, field, warnings);
        applyValue(value, field, instance);
    }

    protected Object getValue(Cell cell, Header header, Field field, List<String> warnings)
            throws AbstractParseException {
        Object value = FieldType.defineType(field.getType()).getCreator().apply(cell);
        if (value == null) {
            if (header.isRequired()) {
                handleException(new MissRequiredFieldException("No required field " + header.getColumnName() + " in cell " + cell
                        .getAddress()
                        .toString()), warnings);
            } else {
                value = header.getDefaultValue();
            }
        }
        return value;
    }

    protected void handleException(AbstractParseException e, List<String> warnings)
            throws AbstractParseException {
        switch (getWarningPolicy()) {
            case STRONG:
                throw e;
            case WEAK:
                warnings.add(e.getMessage());
        }
    }

    protected void applyValue(Object value, Field field, T instance) {
        if (value != null) {
            boolean isAccessible = field.isAccessible();
            field.setAccessible(true);
            try {
                field.set(instance, value);
            } catch (IllegalAccessException ignored) {
            }
            field.setAccessible(isAccessible);
        }
    }
}
