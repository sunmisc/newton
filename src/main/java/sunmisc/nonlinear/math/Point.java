package sunmisc.nonlinear.math;

import java.util.function.UnaryOperator;

public interface Point {
    Number[] point();

    Point transform(int i, UnaryOperator<Double> f);

    default int length() {
        return point().length;
    }
}
