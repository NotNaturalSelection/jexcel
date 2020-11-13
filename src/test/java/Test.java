import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.notnaturalselection.jexcel.exceptions.AbstractParseException;
import org.notnaturalselection.jexcel.parsing.StandardParser;
import org.notnaturalselection.jexcel.parsing.WarningPolicy;
import org.notnaturalselection.jexcel.parsing.dto.ParseResult;

public class Test {
    public static void main(String[] args)
            throws IOException, AbstractParseException {
        StandardParser<ExampleObject, ExampleHeader> parser = new StandardParser<>(
                ExampleObject::new,
                Arrays.asList(ExampleHeader.values())
        );
        ParseResult<ExampleObject> result = parser.parse(new File("path"));
        //or
        parser.parse(new FileInputStream("path"));
        Map<String, List<ExampleObject>> map = result.getResult();
        List<String> warnings = result.getWarnings();
    }
}
