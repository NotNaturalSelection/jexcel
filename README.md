Jexcel
==============
Jexcel is a ```.xls``` and ```.xlsx``` file parser that allows you to easily create and parse Excel files in Java.

### Requirements
Jexcel requires Java 1.8+ and Apache ```org.apache.poi.poi-ooxml``` of version 4.0.0+.

### Installation 
Download ```jexcel-X.X.jar```  with ```org.apache.poi.poi-ooxml``` of version 4.0.0+ and add them to your classpath.
### Getting started
If you have simple data class that has no fields except of 
```String```, ```Date```(```LocalDateTime``` etc.) and primitives you can just use ```org.notnaturalselection.jexcel.parsing.StandardParser```. 
Otherwise, extend  ```org.notnaturalselection.jexcel.parsing.AbstractParser``` and write your own implementation 
of ```parseRow(Row row)``` method.
1. Implement the ```org.notnaturalselection.jexcel.parsing.Header``` interface. Then describe all the fields you want to parse.

   ```java
   public enum ExampleHeader implements Header {
   
       stringField(0, "stringColumn", "", false),
       integerField(1, "intColumn", null, true),
       dateField(2, "dateColumn", null, true);

       private final int columnNumber;
       private final String columnName;
       private final Object defaultValue;
       private final boolean isRequired;
       
       //constructor and getters from interface
       //...
   }
   ```
   > **Note:** ***column name should be equal to the name of the variable it represents.***
   > Otherwise ```org.notnaturalselection.jexcel.exceptions.FieldMappingException``` will be recorded or thrown.
   ```java
   public class ExampleObject {
       private String stringColumn;
       private Integer intColumn;
       private Date dateColumn;
   }
   ```
   
2. Create parser instance providing a ```Supplier<T>``` of your object and all the headers you want to be parsed.
   (See other constructors in a block "Standard parser instance")
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
   //if your file is protected by password
   ParseResult<ExampleObject> result = parser.parse(new File("file.xlsx"), password);
   //or
   ParseResult<ExampleObject> result = parser.parse(new FileInputStream("file.xlsx"), password);
   ```
4. Get your result as ```Map<String, List<ExampleObject>>```. 
   >```String``` keys are names of sheets in source file.
   ```java
   Map<String, List<ExampleObject>> map = result.getResult();
   ```
   Also ```org.notnaturalselection.jexcel.parsing.dto.ParseResult``` contains warnings that appeared in process of parsing.
   ```java
   List<String> warnings = result.getWarnings();
   ```
###Warnings
   Parser accepts ```org.notnaturalselection.jexcel.parsing.WarningPolicy```.
   * ```WarningPolicy.STRONG```. In case of error exception will be thrown.
   * ```WarningPolicy.WEAK```. In case of error parser records a warning that appears in ```ParseResult```.

###Standard parser instance
   Constructor arguments:
   * ```WarningPolicy wp``` - see the "Warnings" block
   * ```int verticalOffset``` - number of rows that will be skipped from the first row
   * ```int horizontalOffset``` - number of columns that will be skipped from the first column
   * ```Supplier<T> supplier``` - constructor or method to create a new instance of result object
   * ```Iterable<H> headers``` - headers that will be parsed

   List of available constructors:
   * ```public StandardParser(WarningPolicy wp, int verticalOffset, int horizontalOffset, Supplier<T> supplier, Iterable<H> headers)```
   * ```public StandardParser(int verticalOffset, int horizontalOffset, Supplier<T> supplier, Iterable<H> headers)```
   * ```public StandardParser(WarningPolicy wp, Supplier<T> supplier, Iterable<H> headers)```  
   * ```public StandardParser(Supplier<T> supplier, Iterable<H> headers)```