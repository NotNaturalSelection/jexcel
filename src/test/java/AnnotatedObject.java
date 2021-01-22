import java.util.Date;

import org.notnaturalselection.jexcel.parsing.annotated.ExcelColumn;

public class AnnotatedObject {
    @ExcelColumn(number = 0, name = "string")
    private String stringColumn;
    @ExcelColumn(number = 1, name = "int")
    private Integer intColumn;
    @ExcelColumn(number = 2, name = "date", isRequired = false)
    private Date dateColumn;

    public String getStringColumn() {
        return stringColumn;
    }

    public void setStringColumn(String stringColumn) {
        this.stringColumn = stringColumn;
    }

    public Integer getIntColumn() {
        return intColumn;
    }

    public void setIntColumn(Integer intColumn) {
        this.intColumn = intColumn;
    }

    public Date getDateColumn() {
        return dateColumn;
    }

    public void setDateColumn(Date dateColumn) {
        this.dateColumn = dateColumn;
    }
}
