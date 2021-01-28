package org.notnaturalselection.jexcel.parsing.standard;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.commons.collections4.CollectionUtils;
import org.notnaturalselection.jexcel.exceptions.AbstractParseException;
import org.notnaturalselection.jexcel.exceptions.FieldMappingException;
import org.notnaturalselection.jexcel.parsing.commons.Header;
import org.notnaturalselection.jexcel.parsing.commons.ReflectionParser;
import org.notnaturalselection.jexcel.parsing.commons.WarningPolicy;

public class StandardParser<T> extends ReflectionParser<T> {

    public static class Builder<T> extends ReflectionParser.Builder<Builder<T>, T> {
        @Override
        public StandardParser<T> build()
                throws AbstractParseException {
            return new StandardParser<>(super.warningPolicy, super.verticalOffset, super.horizontalOffset, super.supplier, headers);
        }

        @Override
        protected Builder<T> self() {
            return this;
        }

        private final Collection<Header> headers;

        public Builder(Collection<Header> headers, Supplier<T> supplier) {
            this.headers = headers;
            super.supplier = supplier;
        }
    }

    private StandardParser(
            WarningPolicy wp, int verticalOffset, int horizontalOffset, Supplier<T> supplier, Collection<Header> headers
    )
            throws FieldMappingException {
        super(verticalOffset, horizontalOffset, wp, supplier, generateMapping(supplier.get().getClass(), headers));
    }

    private static Map<Header, Field> generateMapping(Class<?> aClass, Collection<Header> headers)
            throws FieldMappingException {
        if (CollectionUtils.isEmpty(headers)) {
            throw new FieldMappingException("Header collection is null or empty");
        }
        Map<Header, Field> fieldMapping = new HashMap<>();
        for (Header header : headers) {
            try {
                fieldMapping.put(header, aClass.getDeclaredField(header.getColumnName()));
            } catch (NoSuchFieldException e) {
                throw new FieldMappingException(aClass + " has no field with name " + header.getColumnName());
            }
        }
        return fieldMapping;
    }
}
