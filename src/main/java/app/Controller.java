package app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import sunmisc.nonlinear.Cursor;
import sunmisc.nonlinear.DerivativeFunction;
import sunmisc.nonlinear.iterate.EpsilonIterate;
import sunmisc.nonlinear.iterate.LimitedIterate;
import sunmisc.nonlinear.iterate.NewtonIterate;
import sunmisc.nonlinear.parser.MultiplicationSign;
import sunmisc.nonlinear.parser.Tokenized;
import sunmisc.nonlinear.parser.nodes.MutableNumberNode;
import sunmisc.nonlinear.parser.nodes.Node;
import sunmisc.nonlinear.parser.nodes.Parsed;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.DoubleUnaryOperator;

import static java.lang.String.format;

public class Controller {
    @FXML
    private NumberAxis xAxis = new NumberAxis();
    @FXML
    private NumberAxis yAxis = new NumberAxis();
    @FXML
    private LineChart<Number,Number> funcChart = new LineChart<>(xAxis, yAxis);
    @FXML
    private TextField function;
    @FXML
    private TextField derivative;
    @FXML
    private TextField epsilon;
    @FXML
    private TextField maxIteration;
    @FXML
    private TextField approximation;
    @FXML
    private TextArea logTextArea;


    public void calculateClick(ActionEvent event) {
        DoubleUnaryOperator func = function();
        draw(-15, 15, func);
        int itr = 0 ;
        Cursor<? extends Number> c = new EpsilonIterate(
                new LimitedIterate<>(
                        new NewtonIterate(
                                func,
                                derivative(func),
                                approx()
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
                        Время выполнения = %sms
                        """,
                        c.element().doubleValue(),
                        itr,
                        end
                )
        );
    }
    private int maxIteration() {
        String text = maxIteration.getText();
        return text == null || text.isEmpty() ? 500_000 : Integer.parseInt(text);
    }
    private double approx() {
        String text = approximation.getText();
        return text == null || text.isEmpty()
                ? ThreadLocalRandom.current().nextDouble(0, 1)
                : Double.parseDouble(text);
    }
    private double epsilon() {
        String text = epsilon.getText();
        return text == null || text.isEmpty()
                ? 0.01
                : Double.parseDouble(text);
    }

    private DoubleUnaryOperator function() {
        return toFunction(function.getText());
    }

    private DoubleUnaryOperator derivative(DoubleUnaryOperator src) {
        String text = derivative.getText();
        return text == null || text.isEmpty()
                ? new DerivativeFunction(src)
                : toFunction(text);
    }

    private DoubleUnaryOperator toFunction(String text) {
        AtomicReference<Double> ref = new AtomicReference<>();

        Node node = new Parsed(
                Map.of(
                        // optimized (or mutable map)
                        "x", new MutableNumberNode(ref)
                ),
                new Tokenized(
                        new MultiplicationSign(
                                () -> text
                        )
                )
        );
        return x -> {
            ref.setPlain(x);

            return node.evaluate();
        };
    }

    private void draw(double from, double to, DoubleUnaryOperator func) {
        funcChart.getData().clear();

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        double step = (to - from) / 100;
        for (double x = from; x < to; x += step) {
            double y = func.applyAsDouble(x);
            series.getData().add(new XYChart.Data<>(x, y));
        }

        funcChart.getData().add(series);
        funcChart.setCreateSymbols(false);
        funcChart.setLegendVisible(false);
    }
}
