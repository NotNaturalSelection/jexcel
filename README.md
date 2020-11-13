Jexcel
==============
Jexcel is a ```.xls``` and ```.xlsx``` file parser that allows you to create and parse Excel files in Java.

### Requirements
Jexcel requires Java 1.8+ and uses Apache POI
### Getting started
1. Declare a class that implements the ```org.notnaturalselection.jexcel.parsing.Header``` interface. Then describe all the fields you want to parse.

   ```java
   import org.notnaturalselection.jexcel.parsing.Header;

   public enum ExampleHeader implements Header {
   stringField(0, "stringColumn", null, false),
   integerField(1, "intColumn", null, true),
   dateField(2, "dateColumn", null, true);

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
   ```
   > **Note:** ***column name should be equal to the name of the variable it represents*** 
   ```java
   public class ExampleObject {
       private String stringColumn;
       private Integer intColumn;
       private Date dateColumn;
   }
   ```
   
2. Create parser instance providing a ```Supplier<T>``` of your object and all the headers you want to be parsed.
   ```java
   StandardParser<ExampleObject, ExampleHeader> parser = new StandardParser<>(
        ExampleObject::new,
        Arrays.asList(ExampleHeader.values())
   );
   ```
3. Parse ```java.io.File```, or ```java.io.InputStream``` to get a ```org.notnaturalselection.jexcel.parsing.dto.ParseResult```
   ```java
   ParseResult<ExampleObject> result = parser.parse(new File("file.xlsx"));
   //or
   ParseResult<ExampleObject> result = parser.parse(new FileInputStream("file.xlsx"));
   ```
4. Get your result as ```Map<String, List<ExampleObject>>```. 
   ```java
   Map<String, List<ExampleObject>> map = result.getResult();
   ```
   >```String``` contains the names of sheets in source file.
   
   Also ```org.notnaturalselection.jexcel.parsing.dto.ParseResult``` contains warnings that appeared in process of parsing.
   ```java
   List<String> warnings = result.getWarnings();
   ```
####Warnings
   Parser accepts ```org.notnaturalselection.jexcel.parsing.WarningPolicy```.
   * ```WarningPolicy.STRONG```. In case of error exception will be thrown.
   * ```WarningPolicy.WEAK```. In case of error parser records a warning that appears in ```ParseResult```.
