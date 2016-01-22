package com.github.alkedr.matchers.reporting.reporters;

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
     * Начинает пару ключ-значение.
     *
     * @param name Название проверяемого значения, например название поля, номер элемента в массиве и т. п.
     * @param value Проверяемое значение.
     */
    void beginNode(String name, Object value);  // TODO: beginPresentNode?
    void beginMissingNode(String name);
    void beginBrokenNode(String name, Throwable throwable);

    void correctlyPresent();
    void correctlyMissing();
    void incorrectlyPresent();
    void incorrectlyMissing();

    void passedCheck(String description);
    void failedCheck(String expected, String actual);
    void checkForMissingItem(String description);
    void brokenCheck(String description, Throwable throwable);

    /**
     * Заканчивает узел дерева, начатый методом {@link #beginNode}.
     */
    void endNode();
}
