package com.github.alkedr.matchers.reporting.extraction;

import static com.github.alkedr.matchers.reporting.extraction.ExtractedValueNameUtils.getterNameToPropertyName;

public class GetterMethodByNameExtractingMatcher<T> extends MethodByNameExtractingMatcher<T> {
    public GetterMethodByNameExtractingMatcher(String methodName, Object... arguments) {
        super(methodName, arguments);
    }

    @Override
    public String asString() {
        return getterNameToPropertyName(methodName);
    }
}
