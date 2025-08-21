package de.doubleslash.demo.coverage.module.one;

import java.util.Optional;
import java.util.function.Function;

public class Foo {

    private int i = 7;
    private String s = "jacoco";

    public int fooMethod() {
        return i;
    }

    public String conditionedFooMethod(boolean condition) {
        if (condition) {
            return s;
        }
        else {
            int j = i + 1;
            return s + String.valueOf(j);
        }
    }

    // Java 8 specific features - will fail on Java 1.7
    public Optional<String> getOptionalString() {
        return Optional.ofNullable(s);
    }

    public String processWithLambda() {
        Function<String, String> processor = str -> str.toUpperCase() + "_PROCESSED";
        return processor.apply(s);
    }

}
