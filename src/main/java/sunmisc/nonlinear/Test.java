package sunmisc.nonlinear;

import sunmisc.nonlinear.parser.MultiplicationSign;
import sunmisc.nonlinear.parser.Tokenized;
import sunmisc.nonlinear.parser.nodes.Node;
import sunmisc.nonlinear.parser.nodes.NumberNode;
import sunmisc.nonlinear.parser.nodes.Parsed;

import java.util.Map;

import static java.lang.Math.pow;

public class Test {

    public static void main(String[] args) {
        double x1 = 2, x2 = 12;
        String text = "x^2 - 2*x + 9 + 2 * y^2-8 * y";
        Node node = new Parsed(
                Map.of(
                        "x", new NumberNode(x1),
                        "y", new NumberNode(x2)
                ),
                new Tokenized(
                        new MultiplicationSign(
                                () -> text
                        )
                )
        );
        double a = node.evaluate();
        double b = pow(x1, 2) - 2*x1 + 9 + 2 * pow(x2, 2)-8 * x2;

        System.out.println(a);
        System.out.println(b);
    }
}
