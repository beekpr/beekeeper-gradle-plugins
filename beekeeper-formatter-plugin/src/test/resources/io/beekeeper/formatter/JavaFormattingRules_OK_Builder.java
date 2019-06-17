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
