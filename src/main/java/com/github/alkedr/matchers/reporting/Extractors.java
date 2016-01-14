package com.github.alkedr.matchers.reporting;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static com.github.alkedr.matchers.reporting.ExtractingMatcher.Extractor.ExtractedValue.broken;
import static com.github.alkedr.matchers.reporting.ExtractingMatcher.Extractor.ExtractedValue.missing;
import static com.github.alkedr.matchers.reporting.ExtractingMatcher.Extractor.ExtractedValue.normal;
import static java.lang.reflect.Modifier.isStatic;

class Extractors {
    static class FieldExtractor implements ExtractingMatcher.Extractor {
        private final Field field;

        FieldExtractor(Field field) {
            Validate.notNull(field, "field");
            this.field = field;
        }

        @Override
        public ExtractedValue extractFrom(Object item) {
            try {
                return item == null ? missing() : normal(FieldUtils.readField(field, item, true));
            } catch (IllegalArgumentException | IllegalAccessException e) {
                return broken(e);  // TODO: rethrow?
            }
        }
    }


    static class FieldByNameExtractor implements ExtractingMatcher.Extractor {
        private final String fieldName;

        FieldByNameExtractor(String fieldName) {
            Validate.notNull(fieldName, "fieldName");
            this.fieldName = fieldName;
        }

        @Override
        public ExtractedValue extractFrom(Object item) {
            try {
                return item == null ? missing() : normal(FieldUtils.readField(item, fieldName, true));
            } catch (IllegalArgumentException | IllegalAccessException e) {
                return broken(e);  // TODO: rethrow?
            }
        }
    }


    static class MethodExtractor implements ExtractingMatcher.Extractor {
        private final Method method;
        private final Object[] arguments;

        MethodExtractor(Method method, Object... arguments) {
            Validate.notNull(method, "method");
            Validate.notNull(arguments, "arguments");
            this.method = method;
            this.arguments = arguments;
        }

        @Override
        public ExtractedValue extractFrom(Object item) {
            try {
                method.setAccessible(true);
                return item == null && !isStatic(method.getModifiers()) ? missing() : normal(method.invoke(item, arguments));
            } catch (IllegalArgumentException | IllegalAccessException e) {
                return broken(e);  // TODO: rethrow?
            } catch (InvocationTargetException e) {
                return broken(e.getCause());
            }
        }
    }


    static class MethodByNameExtractor implements ExtractingMatcher.Extractor {
        private final String methodName;
        private final Object[] arguments;

        MethodByNameExtractor(String methodName, Object... arguments) {
            Validate.notNull(methodName, "methodName");
            Validate.notNull(arguments, "arguments");
            this.methodName = methodName;
            this.arguments = arguments;
        }

        @Override
        public ExtractedValue extractFrom(Object item) {
            try {
                return item == null ? missing() : normal(MethodUtils.invokeMethod(item, methodName, arguments));
            } catch (IllegalArgumentException | IllegalAccessException | NoSuchMethodException e) {
                return broken(e);  // TODO: rethrow?
            } catch (InvocationTargetException e) {
                return broken(e.getCause());
            }
        }
    }


    static class ArrayElementExtractor implements ExtractingMatcher.Extractor {
        private final int index;

        ArrayElementExtractor(int index) {
            Validate.isTrue(index >= 0, "index must be positive");
            this.index = index;
        }

        @Override
        public ExtractedValue extractFrom(Object item) {
            try {
                Object[] array = (Object[]) item;
                return item == null || index < 0 || index >= array.length ? missing() : normal(array[index]);
            } catch (ClassCastException e) {
                return broken(e);
            }
        }
    }


    static class ListElementExtractor implements ExtractingMatcher.Extractor {
        private final int index;

        ListElementExtractor(int index) {
            Validate.isTrue(index >= 0, "index must be positive");
            this.index = index;
        }

        @Override
        public ExtractedValue extractFrom(Object item) {
            try {
                List<?> list = (List<?>) item;
                return item == null || index < 0 || index >= list.size() ? missing() : normal(list.get(index));
            } catch (ClassCastException e) {
                return broken(e);
            }
        }
    }


    static class ValueForKeyExtractor implements ExtractingMatcher.Extractor {
        private final Object key;

        ValueForKeyExtractor(Object key) {
            this.key = key;
        }

        @Override
        public ExtractedValue extractFrom(Object item) {
            try {
                if (item == null || !((Map<?, ?>) item).containsKey(key)) {
                    return missing();
                }
                return normal(((Map<?, ?>) item).get(key));
            } catch (ClassCastException e) {
                return broken(e);
            }
        }
    }
}
