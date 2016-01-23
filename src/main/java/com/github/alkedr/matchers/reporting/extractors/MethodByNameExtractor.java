package com.github.alkedr.matchers.reporting.extractors;

import com.github.alkedr.matchers.reporting.keys.MethodByNameKey;
import com.github.alkedr.matchers.reporting.keys.MethodKey;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.Method;

import static com.github.alkedr.matchers.reporting.extractors.Extractors.methodExtractor;

class MethodByNameExtractor implements Extractor {
    private final MethodByNameKey key;

    MethodByNameExtractor(MethodByNameKey key) {
        this.key = key;
    }

    @Override
    public void extractFrom(Object item, ResultListener result) {
        if (item == null) {
            result.missing(key);
        } else {
            // TODO: брать метод, который выше всех в иерархии классов чтобы правильно объединять?
            Method method = MethodUtils.getMatchingAccessibleMethod(
                    item.getClass(),
                    key.getMethodName(),
                    ClassUtils.toClass(key.getArguments())
            );
            if (method == null) {
                result.broken(key, new NoSuchMethodException(item.getClass().getName() + "." + toString()));
            } else {
                methodExtractor(new MethodKey(method, key.getArguments())).extractFrom(item, result);
            }
        }
    }

    @Override
    public void extractFromMissingItem(ResultListener result) {
        extractFrom(null, result);
    }
}
