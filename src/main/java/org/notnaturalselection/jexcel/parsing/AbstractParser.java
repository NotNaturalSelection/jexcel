package org.notnaturalselection.jexcel.parsing;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.notnaturalselection.jexcel.parsing.dto.ParseResult;
import org.notnaturalselection.jexcel.exceptions.AbstractParseException;
import org.notnaturalselection.jexcel.exceptions.IllegalFieldTypeException;

public abstract class AbstractParser<T> {
//    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    protected int verticalOffset;

    protected int horizontalOffset;

    protected WarningPolicy warningPolicy;

    protected ParseResult<T> parseResult;

    public int getVerticalOffset() {
        return verticalOffset;
    }

    public int getHorizontalOffset() {
        return horizontalOffset;
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
            result.getResult().put(sheet.getSheetName(), parseSheet(sheet));
        }
        return result;
    }

    private List<T> parseSheet(Sheet sheet)
            throws AbstractParseException {
        List<T> result = new LinkedList<>();
        for (int i = verticalOffset; i <= sheet.getLastRowNum(); i++) {
            result.add(parseRow(sheet.getRow(i)));
        }
        return result;
    }

    protected abstract T parseRow(Row row)
            throws AbstractParseException;

    protected static Object parseCell(Cell cell) {
        switch (cell.getCellType()) {
            case NUMERIC:
            case FORMULA:
                return cell.getNumericCellValue();
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case STRING:
                return cell.getStringCellValue();
            case ERROR:
            case BLANK:
            case _NONE:
        }
        return null;//fixme
    }

    protected static Double parseNumericCell(Cell cell)
            throws IllegalFieldTypeException {
        if (cell.getCellType().equals(CellType.NUMERIC) || cell.getCellType().equals(CellType.FORMULA)) {
            return cell.getNumericCellValue();
        } else {
            throw new IllegalFieldTypeException("Cell " + cell
                    .getAddress()
                    .formatAsString() + " must be of type" + CellType.NUMERIC + " or " + CellType.FORMULA + ", got " + cell.getCellType().toString());
        }
    }

    protected static String parseStringCell(Cell cell)
            throws IllegalFieldTypeException {
        if (cell.getCellType().equals(CellType.STRING)) {
            return cell.getStringCellValue();
        } else {
            throw new IllegalFieldTypeException("Cell " + cell.getAddress().formatAsString() + " must be of type" + CellType.STRING + ", got " + cell
                    .getCellType()
                    .toString());
        }
    }

    protected static Date parseDateCell(Cell cell)
            throws IllegalFieldTypeException {
        if (cell.getCellType().equals(CellType.NUMERIC)) {
            return cell.getDateCellValue();
        } else {
            throw new IllegalFieldTypeException("Cell " + cell.getAddress().formatAsString() + " must be of type" + CellType.NUMERIC + ", got " + cell
                    .getCellType()
                    .toString());
        }
    }

//    public static String getStringCellAddress(int rowNum, int columnNum) {
//        StringBuilder result = new StringBuilder();
//        do {
//            if (columnNum > ALPHABET.length()) {
//                result.append(ALPHABET.charAt((columnNum  / ALPHABET.length())-1));
//            } else {
//                result.append(ALPHABET.charAt(columnNum  % ALPHABET.length()));
//            }
//            columnNum /= ALPHABET.length();
//        } while (columnNum >0);
//        result.append(rowNum+1);
//        return result.toString();//fixme
//    }

    protected void handleWarning(AbstractParseException e)
            throws AbstractParseException {
        switch (warningPolicy) {
            case STRONG:
                throw e;
            case WEAK:
                parseResult.getWarnings().add(e.getMessage());
        }
    }
}
