package sunmisc.nonlinear.lazy;


import java.util.function.Supplier;

public final class SimpleLazy<E> implements Supplier<E> {
    private Supplier<E> supplier;
    private E result;

    public SimpleLazy(Supplier<E> supplier) {
        this.supplier = supplier;
    }

    @Override
    public E get() {
        E res = result;
        if (res == null) {
            result = res = supplier.get();
            supplier = null;
        }
        return res;
    }

}
