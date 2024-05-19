package sunmisc.nonlinear.parser.nodes;

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
        return parse(queue);
    }
    private Node parse(Queue<String> tokens) {
        Node node = parseFactor(tokens);

        while (!tokens.isEmpty()) {
            String first = tokens.peek();
            switch (first) {
                case "+", "-" -> {
                    String operator = tokens.remove(); // and free
                    Node right = parse(tokens);
                    node = new OperatorNode(operator, node, right);
                }
                case "*", "/", "^" -> {
                    String operator = tokens.remove();
                    Node right = parseFactor(tokens);
                    node = new OperatorNode(operator, node, right);
                }
                default -> { return node; }
            }
        }
        return node;
    }

    private Node parseFactor(Queue<String> tokens) {
        String token = tokens.remove();

        switch (token) {
            case "(", ")" -> {
                Node node = parse(tokens);
                tokens.remove(); // free
                return node;
            }
            default -> {
                Node val = param.get(token);
                return val != null ? val : new NumberNode(token);
            }
        }
    }
}