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
// TODO: убрать beginReport и endReport()?
public interface Reporter {
    /**
     * Начинает пару ключ-значение.
     *
     * @param name Название проверяемого значения, например название поля, номер элемента в массиве и т. п.
     * @param value Проверяемое значение в виде строки. Обычно для примитивных типов - toString, для
     *                      составных объектов - пустая строка.
     */
    // TODO: передавать Object'ы, позволять репортерам самостоятельно решать как их отображать?
    // если нужно указать именно строковое представление, то можно передать строку?
    void beginNode(String name, Object value);
    void beginMissingNode(String name);
    void beginBrokenNode(String name, Throwable throwable);

    // TODO: избавиться от actualPresenceStatus, потмоу что мы его уже указали в beginNode/beginMissingNode?
    void presenceCheck(ReportingMatcher.PresenceStatus expectedPresenceStatus, ReportingMatcher.PresenceStatus actualPresenceStatus);

    void passedCheck(String description);
    void failedCheck(String expected, String actual);
    void checkForMissingItem(String description);
    void brokenCheck(String description, Throwable throwable);

    /**
     * Заканчивает узел дерева, начатый методом {@link #beginNode}.
     */
    void endNode();
}
