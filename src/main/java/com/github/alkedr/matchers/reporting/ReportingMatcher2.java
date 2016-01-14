package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.ReportingMatcher.Reporter.ValueStatus;
import org.hamcrest.Matcher;

import java.util.Iterator;

public interface ReportingMatcher2<T> extends Matcher<T> {
//    Iterator<ExtractedValue2> getKeyMatchersPairs(Object item);
//    Iterator<ExtractedValue2> getKeyMatchersPairsForMissingItem();

    /*
    TODO:
    getKeyMatchersPairs возвращает не итератор, а два итератора: один для простых матчеров, один для ExtractedValue
    ExtractedValue2 тоже содержит два итератора
    При запуске нескольких матчеров:
      - выполняем простые матчеры пока не найдём сложный
      - если нашли сложный, то запоминаем его и продолжаем выполнять простые
      - если нашли второй сложный, то начинаем объединять эти два и все последующие сложные матчеры
      - если дошли до конца и оказалось, что там только один сложный, запускаем его без объединения
     */


    // TODO: нужна ли возможность добавлять простые матчеры для item'а


    interface Checker {
        void normal(Object key, String name, ValueStatus valueStatus);
    }


    // Позволяет объединять
    // TODO: Написать документацию о том, какие поля когда могут быть нулл
    /*interface ExtractedValue2 {
        String getName();
        ExtractionStatus getExtractionStatus();
        PresenceStatus getPresenceStatus();  // разделить на expected и actual?
        Object getValue();
        Throwable getThrowable();
        Iterator<Matcher<Object>> getMatchers();

        // TODO посмотреть старый CompositeCheck

        // .hashCode() и .equals() определяют можно ли объединить два значения
        // можно добавлять ExtractedValue в HashSet
        @Override
        boolean equals(Object other);
        @Override
        int hashCode();
    }*/





    interface ExtractedValue {
        Key getKey();
        Object getValue();
        Iterator<Matcher<Object>> getMatchers();

        default boolean isMergeableWith(ExtractedValue other) {
            return getKey().isSameAs(other.getKey()) && getValue() == other.getValue();  // TODO: .equals для примитивных типов
        }

        // TODO: использовать .hashCode() и .equals() для объединения, хранить всё в HashSet'е?
        // TODO: убрать отдельный Key, Key.isSameAs превращается в .hashCode() и .equals()

        interface Key {
            String getName();
            boolean isSameAs(Key other);
        }
    }




    // Позволяет объединять до извлечения, НО НЕ ДО ИТЕРАЦИИ!
    interface KeyMatchersPair {
        Key getKey();  // для TreeMap, IdentityHashMap и пр. придётся сделать отдельные ключи с правильным isSameAs
        Iterator<Matcher<Object>> getMatchers();

        interface Key {
            String getName();
            boolean isSameAs(Key other);

            // можно вызывать только для item'а, для которого был вызван run(), потому что матчер для Iterable'а хочет
            // итерироваться только один раз и сохранять все значения сразу
            // Для всех остальных
            Object extractFrom(Object item);
        }
    }
}
