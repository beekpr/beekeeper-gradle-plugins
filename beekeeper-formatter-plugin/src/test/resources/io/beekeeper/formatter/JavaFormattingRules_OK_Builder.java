package io.beekeeper.formatter;

import java.util.Arrays;
import java.util.function.Predicate;

public class JavaFormattingRules_OK_Builder {

    // No traling white space anywhere
    public String instanceVariable;

    public void simpleMethod() {
        Object[] array = Arrays.asList()
            .stream()
            .filter(a -> true)
            .filter(b -> false)
            .toArray();
    }

    public void simpleMethod2() {
        Object[] array = Arrays.asList()
            .stream()
            .filter(a -> true)
            .filter(Foo.ofPredicate(true))
            .toArray();
    }

    public void bigChains() {
        new ExampleClass().inifnity("a")
            .inifnity("b")
            .inifnity("a", "b")
            .inifnity(new ExampleClass().inifnity("a"))
            .inifnity("a")
            .inifnity("aaa")
            .inifnity("aaa", "aaa", "aaaa", "aaa", new ExampleClass().inifnity("a"));
    }

    public void biggerIndentaton() {
        ExampleClass example = new ExampleClass();
        if (alwaysFalse(true)) {
            if (alwaysFalse(false)) {
                if (alwaysFalse(alwaysFalse(alwaysFalse(true)))) {
                    Object[] array = Arrays.asList()
                        .stream()
                        .filter(a -> true)
                        .filter(Foo.ofPredicate(true))
                        .filter(Foo.ofPredicate(example.methodWithManyParameters("foo", "bar", "foobar")))
                        .toArray();

                    new ExampleClass().inifnity("a")
                        .inifnity("b")
                        .inifnity("a", "b")
                        .inifnity(new ExampleClass().inifnity("a"))
                        .inifnity("a")
                        .inifnity("aaa")
                        .inifnity("aaa", "aaa", "aaaa", "aaa", new ExampleClass().inifnity("a"));
                }
            }
        }
    }

    /**
     * Dear Code enthusiast. This code does not make any sense
     */
    public boolean alwaysFalse(boolean maybe) {
        if (maybe) {
            return false;
        }

        return false;
    }

    private static class ExampleClass {
        private boolean methodWithManyParameters(String a, String b, String c) {
            return false;
        }

        private boolean methodWithInfiniteParameters(String... a) {
            return false;
        }

        private ExampleClass inifnity(Object... a) {
            return this;
        }
    }

    private static class Foo {
        public static <T> Predicate<T> ofPredicate(boolean b) {
            return new Predicate<T>() {
                @Override
                public boolean test(T t) {
                    return b;
                }
            };
        }
    }
}
