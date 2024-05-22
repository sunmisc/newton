package sunmisc.nonlinear;

import java.util.NoSuchElementException;

public interface Cursor<E> {

    boolean exists();

    E element();

    Cursor<E> next();


    final class Empty<E> implements Cursor<E> {

        @Override public boolean exists() { return false; }

        @Override public Cursor<E> next() { throw new UnsupportedOperationException(); }

        @Override public E element() { throw new NoSuchElementException(); }
    }

}
