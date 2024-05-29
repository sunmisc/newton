package sunmisc.nonlinear.math;

public final class SubPoint extends PointEnvelope {
    private final Point a, b;
    public SubPoint(Point a, Point b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public Number param(int i) {
        return a.param(i).doubleValue() - b.param(i).doubleValue();
    }

    @Override
    public int length() {
        return Math.max(a.length(), b.length());
    }
}
