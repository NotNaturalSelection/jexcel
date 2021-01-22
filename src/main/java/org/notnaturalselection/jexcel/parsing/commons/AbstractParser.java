package org.notnaturalselection.jexcel.parsing.commons;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.notnaturalselection.jexcel.exceptions.AbstractParseException;
import org.notnaturalselection.jexcel.parsing.dto.ParseResult;

public abstract class AbstractParser<T> {

    public int getVerticalOffset() {
        return verticalOffset;
    }

    public void setVerticalOffset(int verticalOffset) {
        this.verticalOffset = verticalOffset;
    }

    public int getHorizontalOffset() {
        return horizontalOffset;
    }

    public void setHorizontalOffset(int horizontalOffset) {
        this.horizontalOffset = horizontalOffset;
    }

    public WarningPolicy getWarningPolicy() {
        return warningPolicy;
    }

    public void setWarningPolicy(WarningPolicy warningPolicy) {
        this.warningPolicy = warningPolicy;
    }

    private int verticalOffset;

    private int horizontalOffset;

    private WarningPolicy warningPolicy;

    public AbstractParser(int verticalOffset, int horizontalOffset, WarningPolicy warningPolicy) {
        this.verticalOffset = verticalOffset;
        this.horizontalOffset = horizontalOffset;
        this.warningPolicy = warningPolicy;
    }


    public ParseResult<T> parse(File file)
            throws IOException, AbstractParseException {
        return parseWorkbook(WorkbookFactory.create(file));
    }

    public ParseResult<T> parse(File file, String password)
            throws IOException, AbstractParseException {
        return parseWorkbook(WorkbookFactory.create(file, password));
    }

    public ParseResult<T> parse(InputStream is)
            throws IOException, AbstractParseException {
        return parseWorkbook(WorkbookFactory.create(is));
    }

    public ParseResult<T> parse(InputStream is, String password)
            throws IOException, AbstractParseException {
        return parseWorkbook(WorkbookFactory.create(is));
    }

    public ParseResult<T> parseWorkbook(Workbook workbook)
            throws AbstractParseException {
        ParseResult<T> result = new ParseResult<>();
        for (Iterator<Sheet> it = workbook.sheetIterator(); it.hasNext(); ) {
            Sheet sheet = it.next();
            result.getValues().put(sheet.getSheetName(), parseSheet(sheet, result.getWarnings()));
        }
        return result;
    }

    private List<T> parseSheet(Sheet sheet, List<String> warnings)
            throws AbstractParseException {
        List<T> result = new LinkedList<>();
        for (int i = verticalOffset; i <= sheet.getLastRowNum(); i++) {
            result.add(parseRow(sheet.getRow(i), warnings));
        }
        return result;
    }

    protected abstract T parseRow(Row row, List<String> warnings)
            throws AbstractParseException;
}
