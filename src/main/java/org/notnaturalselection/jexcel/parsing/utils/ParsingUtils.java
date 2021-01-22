package org.notnaturalselection.jexcel.parsing.utils;

import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.notnaturalselection.jexcel.exceptions.IllegalFieldTypeException;

public class ParsingUtils {
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
}
