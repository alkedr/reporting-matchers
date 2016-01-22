package com.github.alkedr.matchers.reporting.extractors;

import com.github.alkedr.matchers.reporting.keys.Key;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.Method;

import static com.github.alkedr.matchers.reporting.keys.Keys.getterByNameKey;

class GetterByNameExtractor implements Extractor {
    private final String methodName;
    private final Key key;

    GetterByNameExtractor(String methodName) {
        this.methodName = methodName;
        this.key = getterByNameKey(methodName);
    }

    @Override
    public Result extractFrom(Object item) {
        if (item == null) {
            return new Result.Missing(key);
        }
        // TODO: брать метод, который выше всех в иерархии классов чтобы правильно объединять?
        Method method = MethodUtils.getMatchingAccessibleMethod(
                item.getClass(),
                methodName
        );   // TODO: исключения, null
        // TODO: broken если нет такого поля?
        return method == null
                ? new Result.Broken(key, new NoSuchMethodException(item.getClass().getName() + "." + toString()))
                : new GetterExtractor(method).extractFrom(item);
    }

    @Override
    public Result extractFromMissingItem() {
        return new Result.Missing(key);
    }
}
