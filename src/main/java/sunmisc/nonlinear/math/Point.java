package sunmisc.nonlinear.math;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public interface Point {

    Number param(int i);

    Point transform(int i, UnaryOperator<Double> f);

    int length();

    default void forEach(Consumer<Number> action) {
        for (int i = 0, n = length(); i < n; ++i)
            action.accept(param(i));
    }
    default Number[] toArray() {
        int n = length();
        Number[] ns = new Number[n];
        for (int i = 0; i < n; ++i)
            ns[i] = param(i);
        return ns;
    }
}


