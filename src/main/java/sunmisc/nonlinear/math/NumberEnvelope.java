package sunmisc.nonlinear.math;

import java.util.function.Supplier;

public final class NumberEnvelope extends Number {
    private final Supplier<? extends Number> result;

    public NumberEnvelope(Supplier<? extends Number> supplier) {
        this.result = supplier;
    }

    @Override
    public int intValue() {
        return (int) doubleValue();
    }

    @Override
    public long longValue() {
        return (long) doubleValue();
    }

    @Override
    public float floatValue() {
        return (float) doubleValue();
    }

    @Override
    public double doubleValue() {
        return result.get().doubleValue();
    }
    @Override
    public String toString() {
        return String.valueOf(doubleValue());
    }
}
