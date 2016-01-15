package com.github.alkedr.matchers.reporting.extraction;

import java.lang.reflect.Method;

import static com.github.alkedr.matchers.reporting.extraction.ExtractedValueNameUtils.createMethodValueName;

public class SimpleMethodExtractingMatcher<T> extends MethodExtractingMatcher<T> {
    public SimpleMethodExtractingMatcher(Method method, Object... arguments) {
        super(method, arguments);
    }

    @Override
    public String asString() {
        return createMethodValueName(method.getName(), arguments);
    }
}
