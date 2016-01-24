package com.github.alkedr.matchers.reporting.keys;

import org.junit.Test;

import java.util.function.Function;

import static com.github.alkedr.matchers.reporting.keys.LambdaUtils.getArgumentClassFromFunction;
import static org.junit.Assert.assertSame;

public class LambdaUtils_GetArgumentClassFromFunction {
    @Test
    public void methodReference_object() {
        assertSame(Object.class, getArgumentClassFromFunction(Object::getClass));
    }

    @Test
    public void methodReference_finalInnerClass() {
        assertSame(FinalInnerClass.class, getArgumentClassFromFunction(FinalInnerClass::getObject));
    }

    @Test
    public void methodReference_finalInnerClass_methodFromObject() {
        assertSame(Object.class, getArgumentClassFromFunction(Object::getClass));
    }


    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void lambda_object() {
        assertSame(Object.class, getArgumentClassFromFunction((Function<Object, Object>) o -> o.getClass()));
    }

    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void lambda_finalInnerClass() {
        assertSame(FinalInnerClass.class, getArgumentClassFromFunction(
                (Function<FinalInnerClass, Object>) finalInnerClass -> finalInnerClass.getObject()));
    }

    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void lambda_finalInnerClass_methodFromObject() {
        assertSame(Object.class, getArgumentClassFromFunction((Function<Object, Object>) (o) -> o.getClass()));
    }


    @Test
    public void inlineClass_object() {
        assertSame(Object.class, getArgumentClassFromFunction(
                (Function<Object, Object>) new Function<Object, Object>() {
                    @Override
                    public Object apply(Object o) {
                        return o.getClass();
                    }
                })
        );
    }

    @Test
    public void inlineClass_finalInnerClass() {
        assertSame(FinalInnerClass.class, getArgumentClassFromFunction(
                (Function<FinalInnerClass, Object>) new Function<FinalInnerClass, Object>() {
                    @Override
                    public Object apply(FinalInnerClass finalInnerClass) {
                        return finalInnerClass.getObject();
                    }
                })
        );
    }

    @Test
    public void inlineClass_finalInnerClass_methodFromObject() {
        assertSame(Object.class, getArgumentClassFromFunction(
                (Function<Object, Object>) new Function<Object, Object>() {
                    @Override
                    public Object apply(Object o) {
                        return o.getClass();
                    }
                })
        );
    }


    @SuppressWarnings("InnerClassMayBeStatic")
    private final class FinalInnerClass {
        Object getObject() {
            return new Object();
        }
    }
}
