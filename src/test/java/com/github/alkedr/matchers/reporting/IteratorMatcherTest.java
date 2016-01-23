package com.github.alkedr.matchers.reporting;

public class IteratorMatcherTest {
    /*private final ElementChecker elementChecker = mock(ElementChecker.class);
    private final Reporter reporter = mock(Reporter.class);
    private final Object element1 = new Object();
    private final Object element2 = new Object();
    private final Iterable<Object> iterable = asList(element1, element2);
    private final Iterator<CheckResult> beginChecks = new SingletonIterator<>(passedMatcher("1"));
    private final Iterator<CheckResult> element1Checks = new SingletonIterator<>(passedMatcher("2"));
    private final Iterator<CheckResult> element2Checks = new SingletonIterator<>(passedMatcher("3"));
    private final Iterator<CheckResult> endChecks = new SingletonIterator<>(passedMatcher("4"));*/

    /*@Before
    public void setUp() {
        when(elementChecker.begin()).thenReturn(beginChecks);
        when(elementChecker.element(any(), any())).thenReturn(element1Checks).thenReturn(element2Checks);
        when(elementChecker.end()).thenReturn(endChecks);
    }


    // TODO: item == null, item.getClass() != Iterator.class

    @Test
    public void getChecks() {
        Key key1 = Keys.elementKey(0);
        Key key2 = Keys.elementKey(1);

        runAll(new IteratorMatcher<>(() -> elementChecker).getChecks(iterable.iterator()), reporter);

        InOrder inOrder = inOrder(reporter);
        inOrder.verify(reporter).passedCheck("1");
        inOrder.verify(reporter).beginNode(key1, element1);
        inOrder.verify(reporter).passedCheck("2");
        inOrder.verify(reporter).endNode();
        inOrder.verify(reporter).beginNode(key2, element2);
        inOrder.verify(reporter).passedCheck("3");
        inOrder.verify(reporter).endNode();
        inOrder.verify(reporter).passedCheck("4");
        inOrder.verifyNoMoreInteractions();

        // TODO: отдельные тесты на порядок вызова методов ElementChecker'а
        *//*inOrder.verify(elementChecker).begin();
        inOrder.verify(elementChecker).element(eq(key1), same(element1));
        inOrder.verify(elementChecker).element(eq(key2), same(element2));
        inOrder.verify(elementChecker).end();*//*
    }


    @Test
    public void getChecksForMissingItem() {
        runAll(new IteratorMatcher<>(() -> elementChecker).getChecksForMissingItem(), reporter);
        InOrder inOrder = inOrder(reporter);
        inOrder.verify(reporter).passedCheck("1");
        inOrder.verify(reporter).passedCheck("4");

        // TODO: отдельные тесты на порядок вызова методов ElementChecker'а
        *//*InOrder inOrder = inOrder(elementChecker);
        inOrder.verify(elementChecker).begin();
        inOrder.verify(elementChecker).end();
        inOrder.verifyNoMoreInteractions();*//*
    }*/
}
