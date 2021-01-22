import org.notnaturalselection.jexcel.parsing.commons.Header;

public enum ExampleHeader implements Header {
    string(0, "stringColumn", null, false),
    integer(1, "intColumn", null, true),
    date(2, "dateColumn", null, true);

    private final int columnNumber;
    private final String columnName;
    private final Object defaultValue;
    private final boolean isRequired;

    ExampleHeader(int columnNumber, String columnName, Object defaultValue, boolean isRequired) {
        this.columnNumber = columnNumber;
        this.columnName = columnName;
        this.defaultValue = defaultValue;
        this.isRequired = isRequired;
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
}
