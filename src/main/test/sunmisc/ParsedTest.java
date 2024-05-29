package sunmisc;

import org.testng.Assert;
import org.testng.annotations.Test;
import sunmisc.nonlinear.parser.MultiplicationSign;
import sunmisc.nonlinear.parser.Tokenized;
import sunmisc.nonlinear.parser.nodes.Node;
import sunmisc.nonlinear.parser.nodes.NumberNode;
import sunmisc.nonlinear.parser.nodes.Parsed;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.DoubleUnaryOperator;

import static java.lang.Math.*;

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
        double a = node.evaluate();
        double b = Math.pow(2, (6*3-1)) +23/3D * 12 + 1 / 2D;

        Assert.assertEquals(a, b);
    }
    @Test
    public void test2() {
        String text = "2^(6x-1)+23/x * 12 + 1 / x";

        DoubleUnaryOperator a = x -> new Parsed(
                key -> new NumberNode(x),
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
        Assert.assertEquals(q, u);
    }
    @Test
    public void test3() {
        String text = "8384*33333+(333/1222*399 * (2+3*(3+2)))";
        double a = new Parsed(
                new Tokenized(
                        new MultiplicationSign(
                                () -> text
                        )
                )
        ).evaluate();
        double b = 8384*33333+(333/1222D*399 * (2+3*(3+2D)));
        Assert.assertEquals(a, b);
    }
    @Test
    public void test4() {
        double x = 1222;
        String expression = """
                            3 * (3 + (33 + (8 *(3+22*22+(sin(2)))/ 3) * 2) * 3) + 2 +
                            log(x*(2 + 1))^(2+44+(2*3)+(x+1+(3+2)))
                            """;
        Map<String, Node> map = Map.of(
                "x", new NumberNode(x)
        );
        Node parsed = new Parsed(
                map::get,
                new Tokenized(() -> expression)
        );


        double a = parsed.evaluate();
        double b = 3 * (3 + (33 + (8 *(3+22*22+(sin(2)))/ 3) * 2) * 3) + 2 + pow(
                log(x * (2 + 1)), 2+44+(2*3)+(x+1+(3+2)));

        Assert.assertEquals(a, b);
    }
    @Test
    public void test5() {
        double x = 1222;
        String expression = "x^(log(x))";
        Map<String, Node> map = Map.of(
                "x", new NumberNode(x)
        );
        double a = new Parsed(
                map::get,
                new Tokenized(
                        new MultiplicationSign(
                                () -> expression
                        )
                )
        ).evaluate();
        double b = Math.pow(x, Math.log(x));
        Assert.assertEquals(a, b);
    }
}
