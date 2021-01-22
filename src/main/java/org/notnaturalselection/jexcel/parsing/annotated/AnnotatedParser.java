package org.notnaturalselection.jexcel.parsing.annotated;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.notnaturalselection.jexcel.exceptions.FieldMappingException;
import org.notnaturalselection.jexcel.parsing.commons.Header;
import org.notnaturalselection.jexcel.parsing.commons.ReflectionParser;
import org.notnaturalselection.jexcel.parsing.commons.WarningPolicy;

public class AnnotatedParser<T> extends ReflectionParser<T> {

    public static class Builder<T> extends ReflectionParser.Builder<AnnotatedParser.Builder<T>, T> {
        @Override
        public AnnotatedParser<T> build()
                throws FieldMappingException {
            return new AnnotatedParser<>(verticalOffset, horizontalOffset, warningPolicy, supplier, annotatedClass);
        }

        @Override
        protected Builder<T> self() {
            return this;
        }

        private final Class<T> annotatedClass;

        public Builder(Class<T> annotatedClass, Supplier<T> supplier) {
            this.annotatedClass = annotatedClass;
            super.supplier = supplier;
        }
    }

    public AnnotatedParser(int verticalOffset, int horizontalOffset, WarningPolicy warningPolicy, Supplier<T> supplier, Class<T> aClass)
            throws FieldMappingException {
        super(verticalOffset, horizontalOffset, warningPolicy, supplier);
        setFieldMapping(generateMapping(aClass));
    }

    private static Map<Header, Field> generateMapping(Class<?> aClass)
            throws FieldMappingException {
        Map<Header, Field> fieldMapping = new HashMap<>();
        for (Field field : aClass.getDeclaredFields()) {
            ExcelColumn columnInfo = field.getAnnotation(ExcelColumn.class);
            if (columnInfo != null) {
                fieldMapping.put(new HeaderImpl(columnInfo.number(), columnInfo.name(), null, columnInfo.isRequired()), field);
            }
        }
        if (fieldMapping.isEmpty()) {
            throw new FieldMappingException(aClass.toString() + " has no fields annotated with " + ExcelColumn.class.toString());
        }
        return fieldMapping;
    }
}
