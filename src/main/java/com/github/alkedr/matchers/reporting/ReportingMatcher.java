package com.github.alkedr.matchers.reporting;

import org.hamcrest.Matcher;

import java.util.Iterator;

/**
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
public interface ReportingMatcher<T> extends Matcher<T> {

    /**
     * Проверяет item, добавляет всю информацию о проверках в reporter.
     *
     * Обычно этот метод не нужно вызывать напрямую.
     *
     * @param item объект, который нужно проверить
     * @return TODO: описать Matcher<?> и Iterator<KeyValueChecks>
     *
     *     TODO: точно ли нужна возможность добавлять простые матчеры отсюда?
     *     может можно sequence(simpleMatcher, reportingMatcher)?
     */
    Iterator<Object> run(Object item);

    /**
     * Добавляет в reporter информацию о проверках, которые были бы выполнены если бы был вызван метод
     * {@link #run(Object)}.
     *
     * Этот метод используется в случаях наподобие "хотели проверить элемент массива №3, но в массиве было два
     * элемента". В таком случае в отчёте будет отображён элемент №3 и все проверки, которые должны были запуститься
     * на нём.
     *
     * Обычно этот метод не нужно вызывать напрямую.
     *
     * @return
     */
    Iterator<Object> runForMissingItem();


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

        public PresenceStatus getPresenceStatus() {
            return presenceStatus;
        }

        public Object get() {
            return object;
        }

        public String asString() {
            return asString;
        }

        public Throwable getExtractionThrowable() {
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
        private final PresenceStatus expectedPresenceStatus;
        private final ReportingMatcher<?> matcher;

        public Checks(PresenceStatus expectedPresenceStatus, ReportingMatcher<?> matcher) {
            this.expectedPresenceStatus = expectedPresenceStatus;
            this.matcher = matcher;
        }

        public PresenceStatus getExpectedPresenceStatus() {
            return expectedPresenceStatus;
        }

        public ReportingMatcher<?> getMatcher() {
            return matcher;
        }
    }


    enum PresenceStatus {
        PRESENT,
        MISSING,
    }
}
