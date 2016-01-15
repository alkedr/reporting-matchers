package com.github.alkedr.matchers.reporting.extraction;

import java.lang.reflect.Method;

import static com.github.alkedr.matchers.reporting.extraction.ExtractedValueNameUtils.getterNameToPropertyName;

public class GetterMethodExtractingMatcher<T> extends MethodExtractingMatcher<T> {
    public GetterMethodExtractingMatcher(Method method, Object... arguments) {
        super(method, arguments);
    }

    @Override
    public String asString() {
        return getterNameToPropertyName(method.getName());
    }
}
