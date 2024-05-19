package sunmisc.nonlinear;

import java.util.function.DoubleUnaryOperator;

public final class DerivativeFunction implements DoubleUnaryOperator {
    private static final double H = 1e-6;
    private final DoubleUnaryOperator origin;

    public DerivativeFunction(DoubleUnaryOperator origin) {
        this.origin = origin;
    }

    @Override
    public double applyAsDouble(double x) {

        double left = origin.applyAsDouble(x);
        double right = origin.applyAsDouble(x + H);

        return (right - left) / H;
    }
}
