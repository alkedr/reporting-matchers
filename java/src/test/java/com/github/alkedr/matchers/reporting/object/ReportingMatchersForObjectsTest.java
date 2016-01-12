package com.github.alkedr.matchers.reporting.object;

import com.github.alkedr.matchers.reporting.ExtractingMatcherBuilder;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static com.github.alkedr.matchers.reporting.NoOpMatcher.noOp;
import static com.github.alkedr.matchers.reporting.object.ReportingMatchersForObjects.*;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class ReportingMatchersForObjectsTest {
    private final Field myField;
    private final Method getSomethingMethod;

    public ReportingMatchersForObjectsTest() throws NoSuchFieldException, NoSuchMethodException {
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

    public static class MyClass {
        private final int myField = 1;

        public void getSomething() {
        }
    }
}
