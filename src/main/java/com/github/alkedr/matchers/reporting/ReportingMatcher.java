package com.github.alkedr.matchers.reporting;

import com.google.common.collect.Iterators;
import org.apache.commons.collections4.iterators.SingletonIterator;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

import java.util.Iterator;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyIterator;

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

    Checks getChecks(Object item);
    Checks getChecksForMissingItem();


    // TODO: Написать документацию о том, какие поля когда могут быть нулл
    class KeyValueChecks {
        private final Key key;
        private final Value value;
        private final Checks checks;

        public KeyValueChecks(Key key, Value value, Checks checks) {
            // TODO проверять на не-null?
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


    enum PresenceStatus {
        PRESENT,
        MISSING,
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
        private final Throwable extractionThrowable;  // FIXME: это штука ExtractingMatcher'а?

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
        // TODO: throw если presenceStatus != PRESENT?
        public Object get() {
            return object;
        }

        public String asString() {   // TODO: перенести эту логику в Reporter?
            if (presenceStatus == PresenceStatus.MISSING) {
                return "(отсутствует)";
            }
            // TODO: whitelist классов?
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
            return new Value(PresenceStatus.MISSING, null, null, extractionThrowable);
        }
    }


    // TODO: непонятно что делать с expectedPresenceStatus
    // как его объединять?
    // как передать его репортеру в самом начале?
    // отображать presenceStatus как матчер?
    // TODO: Visitor?
    class Checks {
        final Iterator<?> checksIterator;

        public Checks(Iterator<?> checksIterator) {
            this.checksIterator = checksIterator;
        }

        public void run(Object item, Reporter reporter) {
            while (checksIterator.hasNext()) {
                Object next = checksIterator.next();
                if (next instanceof PresenceStatus) {   // TODO: вынести if'ы в метод, принимающий лямбды?
                    runPresenceStatusForPresentItem(item, reporter, (PresenceStatus) next);
                } else if (next instanceof Matcher) {
                    runMatcherForPresentItem(item, reporter, (Matcher<?>) next);
                } else if (next instanceof KeyValueChecks) {
                    runKeyValueChecks(reporter, (KeyValueChecks) next);
                } else {
                    throw new RuntimeException();  // FIXME
                }
            }
        }


        public static Checks noOp() {
            return new Checks(emptyIterator());
        }


        public static Checks presenceStatus(PresenceStatus expectedPresenceStatus) {
            return new Checks(new SingletonIterator<>(expectedPresenceStatus));
        }

        public static Checks present() {
            return presenceStatus(PresenceStatus.PRESENT);
        }

        public static Checks missing() {
            return presenceStatus(PresenceStatus.MISSING);
        }


        public static Checks matchers(Iterator<Matcher<?>> matchersIterator) {
            return new Checks(matchersIterator);
        }

        public static Checks matchers(Iterable<Matcher<?>> matchersIterable) {
            return matchers(matchersIterable.iterator());
        }

        public static Checks matchers(Matcher<?>... matchersArray) {
            return matchers(asList(matchersArray));
        }


        public static Checks keyValueChecks(Iterator<KeyValueChecks> keyValueChecksIterator) {
            return new Checks(keyValueChecksIterator);
        }

        public static Checks keyValueChecks(Iterable<KeyValueChecks> keyValueChecksIterable) {
            return keyValueChecks(keyValueChecksIterable.iterator());
        }

        public static Checks keyValueChecks(KeyValueChecks... keyValueChecksArray) {
            return keyValueChecks(asList(keyValueChecksArray));
        }


        public static Checks sequence(Iterator<Checks> checksIterator) {
            return new Checks(
                    Iterators.concat(
                            Iterators.transform(
                                    checksIterator,
                                    checks -> checks.checksIterator
                            )
                    )
            );
        }

        public static Checks sequence(Iterable<Checks> checksIterable) {
            return sequence(checksIterable.iterator());
        }

        public static Checks sequence(Checks... checksArray) {
            return sequence(asList(checksArray));
        }


        public static Checks merge(Iterator<Checks> checksIterator) {
            return sequence(checksIterator);  //TODO
        }

        public static Checks merge(Iterable<Checks> checksIterable) {
            return sequence(checksIterable.iterator());
        }

        public static Checks merge(Checks... checksArray) {
            return sequence(asList(checksArray));
        }



        private static void runPresenceStatusForPresentItem(Object item, Reporter reporter, PresenceStatus presenceStatus) {
            if (presenceStatus == PresenceStatus.MISSING) {
                reporter.failedCheck("missing", "present");
            } else {
                reporter.passedCheck("is missing");
            }
        }

        private static void runMatcherForPresentItem(Object item, Reporter reporter, Matcher<?> matcher) {
            boolean matches;
            try {
                matches = matcher.matches(item);
            } catch (RuntimeException e) {
                reporter.brokenCheck("", e);
                return;
            }
            if (matches) {
                // TODO: подсовывать свой Description, который отлавливает equalTo и is
                reporter.passedCheck(StringDescription.toString(matcher));
            } else {
                Description mismatchDescription = new StringDescription();
                matcher.describeMismatch(item, mismatchDescription);
                reporter.failedCheck(StringDescription.toString(matcher), mismatchDescription.toString());
            }
        }

        private static void runKeyValueChecks(Reporter reporter, KeyValueChecks kvc) {
            reporter.beginNode(kvc.key().asString(), kvc.value().asString());
            if (kvc.value().extractionThrowable() != null) {
                reporter.brokenCheck("Ошибка при извлечении", kvc.value().extractionThrowable());
            }
            // TODO: missing, broken etc
            if (kvc.value().presenceStatus() == PresenceStatus.PRESENT) {
                kvc.checks().run(kvc.value().get(), reporter);
            } else {
                kvc.checks().runForMissingItem(reporter);
            }
            reporter.endNode();
        }

        private void runForMissingItem(Reporter reporter) {
            while (checksIterator.hasNext()) {
                Object next = checksIterator.next();
                if (next instanceof PresenceStatus) {
                    runPresenceStatusForMissingItem(reporter, (PresenceStatus) next);
                } else if (next instanceof Matcher) {
                    runMatcherForMissingItem(reporter, (Matcher<?>) next);
                } else if (next instanceof KeyValueChecks) {
                    runKeyValueChecks(reporter, (KeyValueChecks) next);
                } else {
                    throw new RuntimeException();  // FIXME
                }
            }
        }

        private static void runPresenceStatusForMissingItem(Reporter reporter, PresenceStatus presenceStatus) {
            if (presenceStatus == PresenceStatus.PRESENT) {
                reporter.failedCheck("present", "missing");
            } else {
                reporter.passedCheck("is present");
            }
        }

        private static void runMatcherForMissingItem(Reporter reporter, Matcher<?> matcher) {
            reporter.failedCheck(StringDescription.toString(matcher), "missing");
        }
    }
}
