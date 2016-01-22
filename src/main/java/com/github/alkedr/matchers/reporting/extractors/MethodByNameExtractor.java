package com.github.alkedr.matchers.reporting.extractors;

import com.github.alkedr.matchers.reporting.keys.Key;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.Method;

import static com.github.alkedr.matchers.reporting.keys.Keys.methodByNameKey;

class MethodByNameExtractor implements Extractor {
    private final String methodName;
    private final Object[] arguments;
    private final Key key;

    MethodByNameExtractor(String methodName, Object... arguments) {
        this.methodName = methodName;
        this.arguments = arguments;
        this.key = methodByNameKey(methodName, arguments);
    }

    @Override
    public Result extractFrom(Object item) {
        if (item == null) {
            return new Result.Missing(key);
        }
        // TODO: брать метод, который выше всех в иерархии классов чтобы правильно объединять?
        Method method = MethodUtils.getMatchingAccessibleMethod(
                item.getClass(),
                methodName,
                ClassUtils.toClass(arguments)
        );   // TODO: исключения, null
        // TODO: broken если нет такого поля?
        return method == null
                ? new Result.Broken(key, new NoSuchMethodException(item.getClass().getName() + "." + toString()))
                : new MethodExtractor(method, arguments).extractFrom(item);
    }

    @Override
    public Result extractFromMissingItem() {
        return new Result.Missing(key);
    }
}
