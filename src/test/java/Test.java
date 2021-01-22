import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.notnaturalselection.jexcel.exceptions.AbstractParseException;
import org.notnaturalselection.jexcel.parsing.annotated.AnnotatedParser;
import org.notnaturalselection.jexcel.parsing.commons.AbstractParser;
import org.notnaturalselection.jexcel.parsing.commons.WarningPolicy;
import org.notnaturalselection.jexcel.parsing.dto.ParseResult;
import org.notnaturalselection.jexcel.parsing.standard.StandardParser;

public class Test {
    public static void main(String[] args)
            throws IOException, AbstractParseException {


        try (FileInputStream source = new FileInputStream("test.xlsx")) {
            StandardParser<ExampleObject> parser = new StandardParser.Builder<>(Arrays.asList(ExampleHeader.values()), ExampleObject::new)
                    .withHorizontalOffset(0)
                    .withVerticalOffset(0)
                    .build();
            ParseResult<ExampleObject> result = parser.parse(source);
            Map<String, List<ExampleObject>> map = result.getValues();
            List<String> warnings = result.getWarnings();
        }
        try (FileInputStream source = new FileInputStream("test.xlsx")) {
            AbstractParser<ExampleObject> parser = new AbstractParser<ExampleObject>(0, 0, WarningPolicy.STRONG) {
                @Override
                protected ExampleObject parseRow(Row row, List<String> warnings)
                        throws AbstractParseException {
                    return new ExampleObject();
                }
            };
            ParseResult<ExampleObject> result = parser.parse(source);
        }

        try (FileInputStream source = new FileInputStream("test.xlsx")) {
            AnnotatedParser<AnnotatedObject> parser = new AnnotatedParser.Builder<>(AnnotatedObject.class, AnnotatedObject::new)
                    .withHorizontalOffset(0)
                    .withVerticalOffset(0)
                    .build();
            ParseResult<AnnotatedObject> result = parser.parse(source);
        }
    }
}
