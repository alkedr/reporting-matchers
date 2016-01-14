package com.github.alkedr.matchers.reporting;

import org.hamcrest.Matcher;

import java.util.Iterator;

/**
 * ReportingMatcher - матчер, который умеет строить отчёт о результатах проверок.
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
 */
public interface ReportingMatcher<T> extends Matcher<T> {

    // Matcher<?> и Iterator<KeyValueChecks>
    /**
     * Проверяет item, добавляет всю информацию о проверках в reporter.
     *
     * @param item объект, который нужно проверить
     * @param reporter reporter, в который нужно добавить информацию о проверках
     */
    Iterator<Object> run(Object item);

    /**
     * Добавляет в reporter информацию о проверках, которые были бы выполнены если бы был вызван метод
     * {@link #run(Object, Reporter)}.
     *
     * Этот метод используется в случаях наподобие "хотели проверить элемент массива №3, но в массиве было два
     * элемента". В таком случае в отчёте будет отображён элемент №3 и все проверки, которые должны были запуститься
     * на нём.
     *
     * Обычно этот метод не нужно вызывать напрямую.
     *
     * @param reporter reporter, в который нужно добавить информацию о проверках
     */
    Iterator<Object> runForMissingItem();


    // TODO: Написать документацию о том, какие поля когда могут быть нулл
    class KeyValueChecks {
        public final Key key;
        public final Value value;
        public final Checks checks;

        public KeyValueChecks(Key key, Value value, Checks checks) {
            this.key = key;
            this.value = value;
            this.checks = checks;
        }
    }


    interface Key {     // Extractor
        String asString();
        @Override boolean equals(Object other);
        @Override int hashCode();
    }


    class Value {     // Extractor
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


    class Checks {    // ExtractingMatcher
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




    // TODO: вынести Reporter отсюда
    /**
     * Reporter - объект, который каким-то образом обрабатывает информацию о проверках от ReportingMatcher'а.
     *
     * TODO: описать структуру отчёта
     * Отчёт имеет древовидную структуру. Каждый узел - пара ключ-значение. Ключ - это
     *
     * TODO: пример кода как создать Reporter и запустить на нём ReportingMatcher
     */
    interface Reporter {

        /**
         * Вызывается перед началом всех проверок. Некоторым реализациям нужно выполнить какие-то действия в начале
         * построения отчёта.
         */
        void beginReport();

        /**
         * Начинает пару ключ-значение.
         *
         * @param keyAsString Ключ, название проверяемого значения, например название поля, номер элемента в массиве и
         *                    т. п.
         * @param valueStatus TODO
         * @param valueAsString Проверяемое значение в виде строки. Обычно для примитивных типов - toString, для
         *                      составных объектов - пустая строка.
         */
        void beginKeyValuePair(String keyAsString, ValueStatus valueStatus, String valueAsString);

        /**
         * Добавляет проверку для текущего узла. Проверка здесь - результат запуска обычного (не-Reporting) матчера.
         * @param status
         * @param description если status == {@link CheckStatus#PASSED}, то describeTo матчера
         *                    если status == {@link CheckStatus#FAILED}, то describeTo + describeMismatch (как сообщение
         *                    об ошибке в AssertionException в {@link org.hamcrest.MatcherAssert#assertThat})
         *                    если status == {@link CheckStatus#BROKEN}, то стектрейс
         */
        void addCheck(CheckStatus status, String description);

        /**
         * Заканчивает узел дерева, начатый методом {@link #beginKeyValuePair}.
         */
        void endKeyValuePair();

        /**
         * Заканчивает построение отчёта, начатое методом {@link #beginReport}. Некоторым реализациям нужно выполнить
         * какие-то действия в конце построения отчёта.
         */
        void endReport();


        enum CheckStatus {
            /**
             * Проверка пройдена.
             */
            PASSED,

            /**
             * Проверка провалена (matches() вернул false).
             */
            FAILED,

            /**
             * Проверка сломана (один из методов matches(), describeTo() и describeMismatch() бросил исключение).
             */
            BROKEN,
        }


        enum ValueStatus {
            /**
             * Значение было извлечено успешно.
             */
            NORMAL,

            /**
             * Значение не было найдено в проверяемом объекте, например проверяли {@link java.util.Map}, искали элемент
             * по ключу, но элемента с таким ключом не было.
             */
            MISSING,

            /**
             * При извлечении значения было брошено исключение.
             */
            BROKEN,
        }
    }
}
