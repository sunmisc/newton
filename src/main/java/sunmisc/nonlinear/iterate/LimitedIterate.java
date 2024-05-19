package sunmisc.nonlinear.iterate;

import sunmisc.nonlinear.Cursor;

public final class LimitedIterate<E> implements Cursor<E> {
    private final Cursor<E> origin;
    private final int start, bound;

    public LimitedIterate(Cursor<E> origin, int max) {
        this(origin, 0, max);
    }
    public LimitedIterate(Cursor<E> origin, int start, int max) {
        this.origin = origin;
        this.bound = max;
        this.start = start;
    }
    @Override
    public boolean exists() {
        return start < bound && origin.exists();
    }
    @Override
    public E element() {
        return origin.element();
    }
    @Override
    public Cursor<E> next() {
        return new LimitedIterate<>(
                origin.next(),
                start + 1,
                bound
        );
    }
}

