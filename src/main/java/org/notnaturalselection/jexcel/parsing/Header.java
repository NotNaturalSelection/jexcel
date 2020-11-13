package org.notnaturalselection.jexcel.parsing;

public interface Header {
    int getColumnNumber();

    String getColumnName();

    Object getDefaultValue();

    boolean isRequired();
}
