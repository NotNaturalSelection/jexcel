package org.notnaturalselection.jexcel.parsing.annotated;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ExcelColumn {
    int number();
    String name();
    boolean isRequired() default true;
}
