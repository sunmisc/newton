package sunmisc.nonlinear.math;

public final class CachedPoint extends PointEnvelope {
    private final Point origin;
    private Number[] cached;

    public CachedPoint(Point origin) {
        this.origin = origin;
    }

    @Override
    public Number param(int i) {
        final Number[] cs = cached();
        final Number x = cs[i];
        return x == null ? cs[i] = origin.param(i) : x;
    }

    @Override
    public int length() {
        return cached().length;
    }

    private Number[] cached() {
        Number[] cs = cached;
        return cs == null ? cached = new Number[origin.length()] : cs;
    }
}
