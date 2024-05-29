package sunmisc.nonlinear.math;

import java.util.Arrays;
import java.util.function.UnaryOperator;

public abstract class PointEnvelope implements Point {

    @Override
    public Point transform(int pos, UnaryOperator<Double> f) {
        Number[] ns = toArray();
        ns[pos] = f.apply(ns[pos].doubleValue());
        return new QPoint(ns);
    }
    @Override
    public String toString() {
        return Arrays.toString(toArray());
    }
}

