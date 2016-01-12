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
import static com.github.alkedr.matchers.reporting.ExtractingMatcherBuilder.extractedValue;
import static java.lang.Character.isUpperCase;
import static java.lang.Character.toLowerCase;
import static java.lang.reflect.Modifier.isStatic;

// используй static import
public class ReportingMatchers {
    // пробивает доступ к private, protected и package-private полям
    // если проверяемый объект имеет неправильный класс, то бросает исключение в matches() ?
    public static <T> ExtractingMatcherBuilder<T> field(Field field) {
        return extractedValue(field.getName(), createFieldExtractor(field));
    }

    // пробивает доступ к private, protected и package-private полям
    // если поле не найдено, то бросает исключение в matches() ?
    public static <T> ExtractingMatcherBuilder<T> field(String fieldName) {
        return extractedValue(fieldName, createFieldByNameExtractor(fieldName));
    }


    // НЕ пробивает доступ к private, protected и package-private полям TODO: пофиксить это?
    // если проверяемый объект имеет неправильный класс, то бросает исключение в matches()
    public static <T> ExtractingMatcherBuilder<T> method(Method method, Object... arguments) {
        return extractedValue(createMethodValueName(method.getName(), arguments), createMethodExtractor(method, arguments));
    }

    // НЕ пробивает доступ к private, protected и package-private полям TODO: пофиксить это?
    // если метод не найден, то бросает исключение в matches()
    public static <T> ExtractingMatcherBuilder<T> method(String methodName, Object... arguments) {
        return extractedValue(createMethodValueName(methodName, arguments), createMethodByNameExtractor(methodName, arguments));
    }


    // как method(), только убирает 'get' и 'is'
    public static <T> ExtractingMatcherBuilder<T> getter(Method method) {
        return extractedValue(getterNameToPropertyName(method.getName()), createMethodExtractor(method));
    }

    // как method(), только убирает 'get' и 'is'
    public static <T> ExtractingMatcherBuilder<T> getter(String methodName) {
        return extractedValue(getterNameToPropertyName(methodName), createMethodByNameExtractor(methodName));
    }


    public static <T> ExtractingMatcherBuilder<T[]> arrayElement(int index) {
        return extractedValue(String.valueOf(index), createArrayElementExtractor(index));
    }

    public static <T> ExtractingMatcherBuilder<List<T>> element(int index) {
        return extractedValue(String.valueOf(index), createListElementExtractor(index));
    }


    public static <K, V> ExtractingMatcherBuilder<Map<K, V>> valueForKey(K key) {
        return extractedValue(String.valueOf(key), createValueForKeyExtractor(key));
    }


    // TODO: тесты
    // TODO: что делать с очень большими toString()?
    static String createMethodValueName(String methodName, Object... arguments) {
        StringBuilder sb = new StringBuilder();
        sb.append(methodName).append('(');
        if (arguments.length > 0) {
            sb.append(arguments[0]);
            for (int i = 1; i < arguments.length; i++) {
                sb.append(", ").append(arguments[i]);   // TODO: брать строки и символы в кавычки?
            }
        }
        sb.append(')');
        return sb.toString();
    }

    static String getterNameToPropertyName(String name) {
        if (name == null) {
            return "";
        }
        if (name.length() > 3 && name.startsWith("get") && isUpperCase(name.charAt(3))) {
            return toLowerCase(name.charAt(3)) + name.substring(4);
        }
        if (name.length() > 2 && name.startsWith("is") && isUpperCase(name.charAt(2))) {
            return toLowerCase(name.charAt(2)) + name.substring(3);
        }
        return name;
    }


    static ExtractingMatcher.Extractor createFieldExtractor(final Field field) {
        Validate.notNull(field, "field");
        return new ExtractingMatcher.Extractor() {
            @Override
            public ExtractedValue extractFrom(Object item) {
                try {
                    return item == null ? missing() : normal(FieldUtils.readField(field, item, true));
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    return broken(e);  // TODO: rethrow?
                }
            }
        };
    }

    static ExtractingMatcher.Extractor createFieldByNameExtractor(final String fieldName) {
        Validate.notNull(fieldName, "fieldName");
        return new ExtractingMatcher.Extractor() {
            @Override
            public ExtractedValue extractFrom(Object item) {
                try {
                    return item == null ? missing() : normal(FieldUtils.readField(item, fieldName, true));
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    return broken(e);  // TODO: rethrow?
                }
            }
        };
    }

    static ExtractingMatcher.Extractor createMethodExtractor(final Method method, final Object... arguments) {
        Validate.notNull(method, "method");
        Validate.notNull(arguments, "arguments");
        return new ExtractingMatcher.Extractor() {
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
        };
    }

    static ExtractingMatcher.Extractor createMethodByNameExtractor(final String methodName, final Object... arguments) {
        Validate.notNull(methodName, "methodName");
        Validate.notNull(arguments, "arguments");
        return new ExtractingMatcher.Extractor() {
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
        };
    }

    static ExtractingMatcher.Extractor createArrayElementExtractor(final int index) {
        return new ExtractingMatcher.Extractor() {
            @Override
            public ExtractedValue extractFrom(Object item) {
                try {
                    Object[] array = (Object[]) item;
                    return item == null || index < 0 || index >= array.length ? missing() : normal(array[index]);
                } catch (ClassCastException e) {
                    return broken(e);
                }
            }
        };
    }

    static ExtractingMatcher.Extractor createListElementExtractor(final int index) {
        return new ExtractingMatcher.Extractor() {
            @Override
            public ExtractedValue extractFrom(Object item) {
                try {
                    List<?> list = (List<?>) item;
                    return item == null || index < 0 || index >= list.size() ? missing() : normal(list.get(index));
                } catch (ClassCastException e) {
                    return broken(e);
                }
            }
        };
    }

    static <K> ExtractingMatcher.Extractor createValueForKeyExtractor(final K key) {
        return new ExtractingMatcher.Extractor() {
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
        };
    }
}
