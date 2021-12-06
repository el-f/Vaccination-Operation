package project.model.util;


/**
 * A generic class holding references to exactly two objects: {@code first}, {@code second}.
 * <br><br/>
 * (Same as Pair in Kotlin, and similar to Tuples in Python).
 *
 * For example:
 * <pre>
 *  Pair&#60;List&#60;Integer&#62;, Pair&#60;HashMap&#60;String, Integer&#62;, Long&#62;&#62; listPairPair =
 *      new Pair&#60;&#62;(
 *              new ArrayList&#60;&#62;(),
 *              new Pair&#60;&#62;( new HashMap&#60;&#62;(), 1L )
 *      );
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
