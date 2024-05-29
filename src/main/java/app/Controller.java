package app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import sunmisc.nonlinear.Cursor;
import sunmisc.nonlinear.iterate.EpsilonIterate;
import sunmisc.nonlinear.iterate.LimitedIterate;
import sunmisc.nonlinear.iterate.NewtonIterate;
import sunmisc.nonlinear.math.Point;
import sunmisc.nonlinear.math.QPoint;
import sunmisc.nonlinear.parser.MultiplicationSign;
import sunmisc.nonlinear.parser.Tokenized;
import sunmisc.nonlinear.parser.nodes.Node;
import sunmisc.nonlinear.parser.nodes.NumberNode;
import sunmisc.nonlinear.parser.nodes.Parsed;
import sunmisc.nonlinear.parser.nodes.RandomNode;

import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.SequencedMap;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.lang.String.format;

public class Controller {
    @FXML
    private TextField function;
    @FXML
    private TextField epsilon;
    @FXML
    private TextField maxIteration;
    @FXML
    private TextField approximation;
    @FXML
    private TextArea logTextArea;

    public void calculateClick(ActionEvent event) {
        SequencedMap<String, Node> params = new LinkedHashMap<>();

        String text = function.getText();
        Node node = new Parsed(
                e -> params.computeIfAbsent(e, k -> new RandomNode(-2, 2)),
                new Tokenized(
                        new MultiplicationSign(
                                () -> text
                        )
                )
        );
        Function<Point, Double> func =  point -> {
            for (int i = 0, n = point.length(); i < n; ++i) {
                String pattern = String.format("X%s", i + 1);
                params.put(pattern, new NumberNode(point.param(i)));
            }
            return node.evaluate();
        };
        Point approx = approx().orElseGet(() -> {
            node.evaluate(); // fil map

            return new QPoint(params.values()
                    .stream()
                    .map(Node::evaluate)
                    .toArray(Double[]::new)
            );
        });
        process(approx, func);
    }

    private void process(Point approx, Function<Point, Double> func) {
        int itr = 0 ;
        Cursor<? extends Point> c = new EpsilonIterate(
                new LimitedIterate<>(
                        new NewtonIterate(
                                func,
                                approx
                        ),
                        maxIteration()
                ),
                func, epsilon()
        );

        final long start = System.currentTimeMillis();
        for (; c.exists(); c = c.next(), itr++);
        final long end = System.currentTimeMillis() - start;

        logTextArea.setText(
                format(
                        """
                        x = %s
                        Итераций = %s
                        Время выполнения = %sms,
                        Точка аппроксмации = %s
                        """,
                        c.element(),
                        itr,
                        end,
                        approx
                )
        );
    }

    private int maxIteration() {
        String text = maxIteration.getText();
        return text == null || text.isEmpty() ? 50_000 : Integer.parseInt(text);
    }
    private double epsilon() {
        String text = epsilon.getText();
        return text == null || text.isEmpty()
                ? 0.01
                : Double.parseDouble(text);
    }

    public Optional<Point> approx() {
        String text = approximation.getText();
        return Optional
                .ofNullable(text)
                .filter(x -> !x.isEmpty())
                .map(x -> new QPoint(Stream.of(x.split(";"))
                        .map(Double::parseDouble)
                        .toArray(Double[]::new))
                );
    }
}
