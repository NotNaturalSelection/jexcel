package org.notnaturalselection.jexcel.parsing;

import java.lang.annotation.*;

@Target(value=ElementType.FIELD)
@Retention(value= RetentionPolicy.RUNTIME)
public @interface ExcelColumn {
    int number();
    String name();
    boolean isRequired() default true;
}
