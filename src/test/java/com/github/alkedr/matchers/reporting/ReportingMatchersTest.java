package com.github.alkedr.matchers.reporting;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static com.github.alkedr.matchers.reporting.NoOpMatcher.noOp;
import static com.github.alkedr.matchers.reporting.ReportingMatchers.*;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class ReportingMatchersTest {
    private final Field myField;
    private final Method getSomethingMethod;

    public ReportingMatchersTest() throws NoSuchFieldException, NoSuchMethodException {
        myField = MyClass.class.getDeclaredField("myField");
        getSomethingMethod = MyClass.class.getDeclaredMethod("getSomething");
    }

    @Test
    public void fieldMatcher() {
        assertReflectionEquals(
                new ExtractingMatcherBuilder<>("myField", createFieldExtractor(myField), noOp()),
                field(myField)
        );
    }

    @Test
    public void fieldByNameMatcher() {
        assertReflectionEquals(
                new ExtractingMatcherBuilder<>("myField", createFieldByNameExtractor("myField"), noOp()),
                field("myField")
        );
    }

    @Test
    public void methodMatcher() {
        assertReflectionEquals(
                new ExtractingMatcherBuilder<>("getSomething(1, 2)", createMethodExtractor(getSomethingMethod), noOp()),
                method(getSomethingMethod, 1, 2)
        );
    }

    @Test
    public void methodByNameMatcher() {
        assertReflectionEquals(
                new ExtractingMatcherBuilder<>("getSomething(1, 2)", createMethodByNameExtractor("getSomething"), noOp()),
                method("getSomething", 1, 2)
        );
    }

    @Test
    public void getterMatcher() {
        assertReflectionEquals(
                new ExtractingMatcherBuilder<>("something", createMethodExtractor(getSomethingMethod), noOp()),
                getter(getSomethingMethod)
        );
    }

    @Test
    public void getterByNameMatcher() {
        assertReflectionEquals(
                new ExtractingMatcherBuilder<>("something", createMethodByNameExtractor("getSomething"), noOp()),
                getter("getSomething")
        );
    }

    @Test
    public void arrayElementMatcher() {
        assertReflectionEquals(
                new ExtractingMatcherBuilder<>("1", createArrayElementExtractor(1), noOp()),
                arrayElement(1)
        );
    }

    @Test
    public void listElementMatcher() {
        assertReflectionEquals(
                new ExtractingMatcherBuilder<>("1", createListElementExtractor(1), noOp()),
                element(1)
        );
    }

    @Test
    public void valueForKeyMatcher() {
        assertReflectionEquals(
                new ExtractingMatcherBuilder<>("1", createValueForKeyExtractor(1), noOp()),
                valueForKey(1)
        );
    }

    public static class MyClass {
        private final int myField = 1;

        public void getSomething() {
        }
    }
}
