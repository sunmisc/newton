package app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import sunmisc.nonlinear.Cursor;
import sunmisc.nonlinear.iterate.EpsilonIterate;
import sunmisc.nonlinear.iterate.LimitedIterate;
import sunmisc.nonlinear.iterate.NewtonIterate;
import sunmisc.nonlinear.math.QPoint;
import sunmisc.nonlinear.math.Point;
import sunmisc.nonlinear.parser.MultiplicationSign;
import sunmisc.nonlinear.parser.Tokenized;
import sunmisc.nonlinear.parser.nodes.*;

import java.util.*;
import java.util.function.Function;

import static java.lang.String.format;

public class Controller {
    private static final int RESOLUTION = 50;
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
        Function<Point, Double> func = function();
        draw(-15, 15, func);
        int itr = 0 ;
        Cursor<? extends Point> c = new EpsilonIterate(
                new LimitedIterate<>(
                        new NewtonIterate(
                                func,
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
                        c.element(),
                        itr,
                        end
                )
        );
    }
    private int maxIteration() {
        String text = maxIteration.getText();
        return text == null || text.isEmpty() ? 50_000 : Integer.parseInt(text);
    }
    private Point approx() {
        String text = approximation.getText();
        return new QPoint(
                text == null || text.isEmpty()
                        ? new Double[]{1D, 0D}
                        : Arrays
                        .stream(text.split(";"))
                        .map(Double::parseDouble)
                        .toArray(Double[]::new)
        );
    }
    private double epsilon() {
        String text = epsilon.getText();
        return text == null || text.isEmpty()
                ? 0.01
                : Double.parseDouble(text);
    }

    private Function<Point, Double> function() {
        return toFunction(function.getText());
    }

    /*private DoubleUnaryOperator derivative(DoubleUnaryOperator src) {
        String text = derivative.getText();
        return text == null || text.isEmpty()
                ? new DerivativeFunction(src)
                : toFunction(text);
    }*/

    private Function<Point, Double> toFunction(String text) {
        Map<String, Node> param = new HashMap<>();
        // x * x - 2 * x + 9 + 2 * y * y - 8 * y
        Node node = new Parsed(
                param,
                new Tokenized(
                        new MultiplicationSign(
                                () -> text
                        )
                )
        );
        return point -> {
            Number[] vls = point.point();
            System.out.println(Arrays.toString(vls));
            for (int i = 0, n = vls.length; i < n; ++i) {
                String pattern = String.format("X%s", i + 1);
                param.put(pattern, new NumberNode(vls[i]));
            }
            return node.evaluate();
        };
    }

    private void draw(double from, double to, Function<Point, Double> func) {
        funcChart.getData().clear();

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        double step = (to - from) / RESOLUTION;
        for (double x = from; x < to; x += step) {
            for (double y = from; y < to; y += step) {
                double val = func.apply(new QPoint(x,y));
                series.getData().add(new XYChart.Data<>(x, val));
            }
        }

        funcChart.getData().add(series);
        funcChart.setCreateSymbols(false);
        funcChart.setLegendVisible(false);
    }
}
