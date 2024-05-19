package sunmisc.nonlinear;

public interface Cursor<E> {

    boolean exists();

    E element();

    Cursor<E> next();
}
