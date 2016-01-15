package com.github.alkedr.matchers.reporting.extraction;

public class SimpleMethodByNameExtractingMatcher<T> extends MethodByNameExtractingMatcher<T> {
    public SimpleMethodByNameExtractingMatcher(String methodName, Object... arguments) {
        super(methodName, arguments);
    }

    @Override
    public String asString() {
        return ExtractedValueNameUtils.createMethodValueName(methodName, arguments);
    }
}
