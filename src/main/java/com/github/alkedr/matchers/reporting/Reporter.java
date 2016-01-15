package com.github.alkedr.matchers.reporting;

/**
 * Reporter - объект, который каким-то образом обрабатывает информацию о проверках от ReportingMatcher'а.
 *
 * TODO: описать структуру отчёта
 * Отчёт имеет древовидную структуру. Каждый узел - пара ключ-значение. Ключ - это
 *
 * TODO: пример кода как создать Reporter и запустить на нём ReportingMatcher
 */
// TODO: написать в доках интерфейсов как их реализовывать
public interface Reporter {

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
