/**
 * Created by Ramprasad on 5/3/14.
 */
public class Either<V> {
    private final V val;
    private final Exception ex;

    public Either(V aNR) {
        if (aNR == null) throw new NullPointerException("Must have either a hasValue or an error.");
        val = aNR;
        ex = null;
    }

    public Either(Exception anE) {
        if (anE == null) throw new NullPointerException("Must have either a hasValue or an error.");
        val = null;
        ex = anE;
    }

    public boolean hasValue() {
        return val != null;
    }

    public V right() {
        return val;
    }

    public V getOrElse( final V defaultValue ) {
        return hasValue() ? val : defaultValue;
    }

    public Exception left() {
        return ex;
    }

    public interface Map<S, T> {
        public T map(final S s);
    }

    public <O> Either<O> rightMap(Map<V, O> m) {
        if (hasValue()) {
            try {
                return new Either<O>(m.map(this.val));
            }
            catch (Exception e) {
                return new Either<O>(e);
            }
        }
        return new Either<O>(ex);
    }

}