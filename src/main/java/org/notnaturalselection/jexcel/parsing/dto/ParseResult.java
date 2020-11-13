package org.notnaturalselection.jexcel.parsing.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParseResult<T> {
    private final Map<String, List<T>> result = new HashMap<>();
    private final List<String> warnings = new ArrayList<>();

    public Map<String, List<T>> getResult() {
        return result;
    }

    public List<String> getWarnings() {
        return warnings;
    }
}
