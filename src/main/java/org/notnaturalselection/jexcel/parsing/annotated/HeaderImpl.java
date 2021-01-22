package org.notnaturalselection.jexcel.parsing.annotated;

import org.notnaturalselection.jexcel.parsing.commons.Header;

public class HeaderImpl implements Header {

    private int columnNumber;
    private String columnName;
    private Object defaultValue = null;
    private boolean isRequired = true;

    public void setColumnNumber(int columnNumber) {
        this.columnNumber = columnNumber;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setRequired(boolean required) {
        isRequired = required;
    }

    @Override
    public int getColumnNumber() {
        return columnNumber;
    }

    @Override
    public String getColumnName() {
        return columnName;
    }

    @Override
    public Object getDefaultValue() {
        return defaultValue;
    }

    @Override
    public boolean isRequired() {
        return isRequired;
    }

    public HeaderImpl() {
    }

    public HeaderImpl(int columnNumber, String columnName, Object defaultValue, boolean isRequired) {
        this.columnNumber = columnNumber;
        this.columnName = columnName;
        this.defaultValue = defaultValue;
        this.isRequired = isRequired;
    }
}
