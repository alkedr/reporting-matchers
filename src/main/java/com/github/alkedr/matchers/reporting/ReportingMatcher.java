package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;
import org.hamcrest.Matcher;

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
    void run(Object item, SafeTreeReporter safeTreeReporter);
    void runForMissingItem(SafeTreeReporter safeTreeReporter);
}
