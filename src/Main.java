import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class Main extends Application {

    public static double attackerSuccessProbability(double q) {
        double p = 1.0 - q;
        int z = 0;

        while (true) {
            double lambda = z * (q / p);
            double sum = 1.0;

            for (int k = 0; k <= z; k++) {
                double poisson = Math.exp(-lambda);

                for (int i = 1; i <= k; i++) {
                    poisson *= lambda / i;
                }

                sum -= poisson * (1 - Math.pow(q / p, z - k));
            }

            if (Math.abs(sum) > 0.001) {
                z++;
            } else {
                return z;
            }
        }
    }

    public static Map<Double, Integer> calculateConfirmationBlocks() {
        Map<Double, Integer> data = new HashMap<>();

        for (double q = 0.1; q <= 0.45; q += 0.05) {
            int minBlocks = (int) attackerSuccessProbability(q);
            data.put(q, minBlocks);
            System.out.printf("Частка зловмисників: %.2f, Мінімальна кількість блоків: %d%n", q, minBlocks);
        }

        return data;
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Double Spend Attack Visualization");

        // Створення осей
        NumberAxis xAxis = new NumberAxis(0.1, 0.45, 0.05);
        xAxis.setLabel("Частка зловмисників (q)");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Кількість блоків підтвердження");

        // Створення графіка
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Залежність кількості блоків підтвердження від частки зловмисників");

        // Додавання даних до графіка
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Блоки підтвердження");

        Map<Double, Integer> data = calculateConfirmationBlocks();
        for (Map.Entry<Double, Integer> entry : data.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        lineChart.getData().add(series);

        // Створення сцени і додавання графіка
        Scene scene = new Scene(lineChart, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}



















