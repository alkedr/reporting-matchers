package com.github.alkedr.matchers.reporting;

import org.hamcrest.Matcher;

import java.util.Iterator;

/**
 *
 * 2 виде матчеров:
 *   ExtractingMatcher - извлекает 1 значение и проверяет его
 *   IteratingMatcher - перечисляет все значения, передаёт их своим миньонам, которые добавляют проверки
 *
 *   ExtractingMatcher можно рассматривать как частный случай IteratingMatcher'а
 *   Он перечисляет все значения (одно), и добавляет для него одну проверку
 *
 *
 *
 *
 * ----ReportingMatcher - матчер, который умеет строить отчёт о результатах проверок.----
 *
 * -------
 * Преимущества перед matches() + describeMismatch():
 *   - Можно использовать кастомные генераторы отчётов и генерировать отчёты в разных форматах. Это позволяет
 *     генерировать большие отчёты без потери читаемости.
 *   - Можно фильтровать отчёты, например можно удалять из отчёта все пройденные проверки, и оставить только
 *     проваленные.
 *   - Проверка и построение отчёта - один шаг
 *     - Можно не бояться сайд-эффектов (у обычных матчеров matches() теоретически может повлиять на последующие
 *       describeMismatch()).  (!! у run могут быть сайд-эффекты, не аргумент !!)
 *     - Можно не бояться противоречий между matches() и describeMismatch(). У обычных матчеров matches() может,
 *       например, вернуть false, а describeMismatch() после этого может сказать, что всё было правильно.
 * -------
 *
 * Интерфейс ReportingMatcher расширяет интерфейс Matcher, экземпляры ReportingMatcher можно передавать в assertThat.
 *
 * TODO: ссылка на метод, который нужно вызывать чтобы запустить ReportingMatcher, пример кода
 */
// TODO: написать в доках интерфейсов как их реализовывать
public interface ReportingMatcher<T> extends Matcher<T> {


    Checks getChecks(Value value);


    // TODO: Написать документацию о том, какие поля когда могут быть нулл
    class KeyValueChecks {
        private final Key key;
        private final Value value;
        private final Checks checks;

        public KeyValueChecks(Key key, Value value, Checks checks) {
            this.key = key;
            this.value = value;
            this.checks = checks;
        }

        public Key key() {
            return key;
        }

        public Value value() {
            return value;
        }

        public Checks checks() {
            return checks;
        }
    }


    interface Key {
        String asString();
        @Override boolean equals(Object other);
        @Override int hashCode();
    }


    class Value {
        private final PresenceStatus presenceStatus;
        private final Object object;
        private final String asString;
        private final Throwable extractionThrowable;

        Value(PresenceStatus presenceStatus, Object object, String asString, Throwable extractionThrowable) {
            this.presenceStatus = presenceStatus;
            this.object = object;
            this.asString = asString;
            this.extractionThrowable = extractionThrowable;
        }

        public PresenceStatus presenceStatus() {
            return presenceStatus;
        }

        // TODO: переименовать
        // TODO: throw если presenceStatus != PRESENT?  Optional?
        public Object get() {
            return object;
        }

        public String asString() {
            return asString == null ? String.valueOf(object) : asString;
        }

        public Throwable extractionThrowable() {
            return extractionThrowable;
        }

        public static Value present(Object object) {
            return present(object, null);
        }

        public static Value present(Object object, String asString) {
            return new Value(PresenceStatus.PRESENT, object, asString, null);
        }

        public static Value missing() {
            return new Value(PresenceStatus.MISSING, null, null, null);
        }

        public static Value broken(Throwable extractionThrowable) {
            return new Value(null, null, null, extractionThrowable);
        }
    }


    class Checks {
        // TODO: сделать Iterator<Object>, но спрятать его в этом классе? даже перенести сюда объединение?
        // abstract class Checks implements Iterator<Object>, несколько более дружелюбных реализаций

        // Iterator<Matcher<PresenceStatus>> ?
        private final PresenceStatus expectedPresenceStatus;   // nullable
        private final Iterator<Matcher<?>> matcherIterator;
        private final Iterator<KeyValueChecks> keyValueChecksIterator;

        public Checks(PresenceStatus expectedPresenceStatus, Iterator<Matcher<?>> matcherIterator,
                      Iterator<KeyValueChecks> keyValueChecksIterator) {
            this.expectedPresenceStatus = expectedPresenceStatus;
            this.matcherIterator = matcherIterator;
            this.keyValueChecksIterator = keyValueChecksIterator;
        }

        public PresenceStatus expectedPresenceStatus() {
            return expectedPresenceStatus;
        }

        public Iterator<Matcher<?>> getMatcherIterator() {
            return matcherIterator;
        }

        public Iterator<KeyValueChecks> getKeyValueChecksIterator() {
            return keyValueChecksIterator;
        }


        // TODO: run(item, reporter)?
    }


    enum PresenceStatus {
        PRESENT,
        MISSING,
    }



    abstract class Checks2 implements Iterator<Object> {

        // TODO: run(item, reporter)?
    }

    // TODO: если сделать поле, то будет проще, можно будет заюзать Iterators.*

    // TODO: переименовать
    class SequencedChecks extends Checks2 {
        public SequencedChecks(Iterator<Checks2> checks2s) {
        }

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Object next() {
            return null;
        }
    }

    class MergedChecks extends Checks2 {
        public MergedChecks(Iterator<Checks2> checks2s) {
        }

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Object next() {
            return null;
        }
    }

    class PresenceCheck extends Checks2 {
        public PresenceCheck(PresenceStatus presenceStatus) {
        }

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Object next() {
            return null;
        }
    }

    class SimpleMatchersCheck extends Checks2 {
        public SimpleMatchersCheck(Iterator<Matcher<?>> matcherIterator) {
        }

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Object next() {
            return null;
        }
    }

    class KeyValueChecksCheck extends Checks2 {
        public KeyValueChecksCheck(Iterator<KeyValueChecks> matcherIterator) {
        }

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Object next() {
            return null;
        }
    }
}
