package sunmisc.nonlinear.iterate;

import sunmisc.nonlinear.Cursor;
import sunmisc.nonlinear.DerivativeFunction;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.DoubleUnaryOperator;

public final class NewtonIterate implements Cursor<Double> {
    private final DoubleUnaryOperator function, derivative;
    private final double x;

    public NewtonIterate(DoubleUnaryOperator function,
                         DoubleUnaryOperator derivative,
                         double initialGuess) {
        this.function = function;
        this.derivative = derivative;
        this.x = initialGuess;
    }

    @Override public boolean exists() { return true; }

    @Override public Double element() { return x; }

    @Override
    public Cursor<Double> next() {
        double fx = function.applyAsDouble(x);
        double dfx = derivative.applyAsDouble(x);

        double deltaX = fx / dfx;

        return new NewtonIterate(
                function,
                derivative,
                x - deltaX
        );
    }

    public NewtonIterate(DoubleUnaryOperator function) {
        this(function, new DerivativeFunction(function));
    }
    public NewtonIterate(DoubleUnaryOperator function, double x) {
        this(function, new DerivativeFunction(function), x);
    }

    public NewtonIterate(DoubleUnaryOperator function,
                         DoubleUnaryOperator derivative) {
        this(function, derivative, ThreadLocalRandom
                .current()
                .nextDouble(0, 1));
    }
}
