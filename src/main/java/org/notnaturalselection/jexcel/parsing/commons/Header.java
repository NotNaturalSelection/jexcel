package org.notnaturalselection.jexcel.parsing.commons;

public interface Header {
    int getColumnNumber();

    String getColumnName();

    Object getDefaultValue();

    boolean isRequired();
}
