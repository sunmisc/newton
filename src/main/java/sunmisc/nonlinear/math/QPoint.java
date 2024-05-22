package sunmisc.nonlinear.math;

import java.util.Arrays;
import java.util.function.UnaryOperator;

public final class QPoint implements Point {
    private final Number[] numbers;

    public QPoint(Number... numbers) {
        this.numbers = numbers;
    }
    @Override
    public Number[] point() {
        return numbers;
    }

    @Override
    public Point transform(int i, UnaryOperator<Double> f) {
        Number[] copy = numbers.clone();
        Number p = copy[i];
        copy[i] = f.apply(p.doubleValue());
        return new QPoint(copy);
    }

    @Override
    public String toString() {
        return Arrays.toString(numbers);
    }
}
