package sunmisc.nonlinear.math;

public final class Normalize extends Number {
    private final Point point;
    public Normalize(Point point) {
        this.point = point;
    }

    @Override
    public double doubleValue() {
        double sum = 0;
        for (Number value : point.point())
            sum += Math.pow(value.doubleValue(), 2);
        return Math.sqrt(sum);
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

}
