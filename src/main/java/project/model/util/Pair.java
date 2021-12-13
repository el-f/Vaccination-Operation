package project.model.util;


/**
 * A generic class holding references to exactly two objects: {@code first}, {@code second}.
 * <br><br/>
 * (Same as Pair in Kotlin, and similar to Tuples in Python).
 *
 * For example:
 * <pre>
 *  Pair&#60;Integer, String&#62; intStrPair = new Pair&#60;&#62;(42, "I love refrigerators!");
 * </pre>
 *
 * @author Elazar Fine  - github.com/Elfein7Night
 */
@SuppressWarnings("unused") //  All methods are here for completion sake, even if unused.
public class Pair<F, S> {

    private F first;
    private S second;

    public Pair(F _first, S _second) {
        first = _first;
        second = _second;
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }

    public void setFirst(F first) {
        this.first = first;
    }

    public void setSecond(S second) {
        this.second = second;
    }
}
