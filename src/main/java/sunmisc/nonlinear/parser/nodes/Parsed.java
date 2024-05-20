package sunmisc.nonlinear.parser.nodes;

import sunmisc.nonlinear.parser.Tokenized;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public final class Parsed implements Node {
    private final Iterable<String> tokens;
    private final Map<String, Node> param;

    public Parsed(String... tokens) {
        this(Map.of(), List.of(tokens));
    }
    public Parsed(Map<String, Node> param, String... tokens) {
        this(param, List.of(tokens));
    }
    public Parsed(Iterable<String> tokens) {
        this(Map.of(), tokens);
    }

    public Parsed(Map<String, Node> param, Iterable<String> tokens) {
        this.tokens = tokens;
        this.param = param;
    }

    @Override
    public double evaluate() {
        return parse().evaluate();
    }

    @Override
    public String asString() {
        return parse().asString();
    }

    private Node parse() {
        Queue<String> queue = new ArrayDeque<>();
        tokens.forEach(queue::add);
        return next(queue);
    }
    private Node next(Queue<String> tokens) {
        Node node = unary(tokens);

        while (!tokens.isEmpty()) {
            String first = tokens.peek();
            switch (first) {
                case "+", "-" -> {
                    String operator = tokens.remove(); // and free
                    Node right = next(tokens);
                    node = new OperatorNode(operator, node, right);
                }
                case "*", "/", "^"  -> {
                    String operator = tokens.remove();
                    Node right = unary(tokens);
                    node = new OperatorNode(operator, node, right);
                }
                default -> { return node; }
            }
        }
        return node;
    }
    private Node unary(Queue<String> tokens) {
        String token = tokens.remove();

        switch (token) {
            case "(" -> {
                try {
                    return next(tokens);
                } finally {
                    tokens.remove(); // )
                }
            }
            case "sin" -> {
                return new SinNode(unary(tokens));
            }
            case "cos" -> {
                return new CosNode(unary(tokens));
            }
            case "log" -> {
                return new LogNode(unary(tokens));
            }
            default -> {
                Node val = param.get(token);
                return val != null ? val : new NumberNode(token);
            }
        }
    }
    public static void main(String[] args) {
        double x = 1222;
        String expression = "x^(log(x))";
        Parsed parsed1 = new Parsed(
                Map.of("x", new NumberNode(x)),
                new Tokenized(() -> expression)
        );


        double a = parsed1.evaluate();
        double b = Math.pow(x, Math.log(x));
        System.out.println(a + " " + b);
    }
}