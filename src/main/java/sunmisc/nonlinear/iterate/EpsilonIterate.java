package sunmisc.nonlinear.iterate;

import sunmisc.nonlinear.Cursor;

import java.util.function.DoubleUnaryOperator;

import static java.lang.Math.abs;

public final class EpsilonIterate implements Cursor<Double> {
    private final Cursor<Double> itr;
    private final DoubleUnaryOperator origin;
    private final double epsilon;

    public EpsilonIterate(Cursor<Double> itr,
                          DoubleUnaryOperator origin,
                          double epsilon) {
        this.itr = itr;
        this.origin = origin;
        this.epsilon = epsilon;
    }

    @Override
    public boolean exists() {
        return itr.exists() && abs(
                origin.applyAsDouble(itr.element())
        ) > epsilon;
    }

    @Override
    public Double element() {
        return itr.element();
    }

    @Override
    public Cursor<Double> next() {
        return new EpsilonIterate(itr.next(), origin, epsilon);
    }
}
