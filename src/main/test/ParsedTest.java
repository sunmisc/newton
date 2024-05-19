import org.testng.annotations.Test;
import sunmisc.nonlinear.parser.MultiplicationSign;
import sunmisc.nonlinear.parser.Tokenized;
import sunmisc.nonlinear.parser.nodes.Node;
import sunmisc.nonlinear.parser.nodes.NumberNode;
import sunmisc.nonlinear.parser.nodes.Parsed;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.DoubleUnaryOperator;

@Test
public class ParsedTest {

    @Test
    public void test1() {
        String text = "2^(6*3-1)+23/3 * 12 + 1 / 2";
        Node node = new Parsed(
                new Tokenized(
                        new MultiplicationSign(
                                () -> text
                        )
                )
        );
        double x = node.evaluate();
        double y = Math.pow(2, (6*3-1)) +23/3D * 12 + 1 / 2D;

        if (x != y)
            throw new AssertionError();
    }
    @Test
    public void test2() {
        String text = "2^(6x-1)+23/x * 12 + 1 / x";
        DoubleUnaryOperator a = x -> new Parsed(
                Map.of("x", new NumberNode(x)),
                new Tokenized(
                        new MultiplicationSign(
                                () -> text
                        )
                )
        ).evaluate();
        DoubleUnaryOperator b = x -> Math.pow(2, (6*x-1)) +23/x * 12 + 1 / x;

        double x = ThreadLocalRandom.current().nextDouble(-100, 100);

        double q = a.applyAsDouble(x);
        double u = b.applyAsDouble(x);
        if (q != u)
            throw new AssertionError(q + " != " + u);
    }
    @Test
    public void test3() {
        String text = "8384*33333+(333/1222*399 * (2+3*(3+2)))";
        double q = new Parsed(
                new Tokenized(
                        new MultiplicationSign(
                                () -> text
                        )
                )
        ).evaluate();
        double u = 8384*33333+(333/1222D*399 * (2+3*(3+2D)));
        if (q != u)
            throw new AssertionError(q + " != " + u);
    }
}
