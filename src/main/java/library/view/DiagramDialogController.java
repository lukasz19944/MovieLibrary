package library.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import library.dao.MovieDao;
import library.model.Movie;

import java.time.Year;
import java.util.List;

public class DiagramDialogController {

    @FXML
    private LineChart<String, Float> lineChart;
    @FXML
    private CategoryAxis xAxis;

    private ObservableList<String> years = FXCollections.observableArrayList();

    int minYear;
    int maxYear;

    @FXML
    private void initialize() {
        MovieDao mDao = new MovieDao();
        minYear = mDao.getMinYear();
        maxYear = Year.now().getValue();

        for (int i = minYear; i <= maxYear; i++) {
            years.add(String.valueOf(i));
        }

        xAxis.setCategories(years);
    }

    public void setAverageRateForYear(List<Movie> movies) {
        int[] yearCounter = new int[maxYear - minYear];
        float[] rates = new float[maxYear - minYear];
        float[] avgRates = new float[maxYear - minYear];

        for (Movie movie : movies) {
            yearCounter[movie.getReleaseDate() - minYear]++;
            rates[movie.getReleaseDate() - minYear] += movie.getRate();
        }

        for (int i = 0; i < yearCounter.length; i++) {
            if (yearCounter[i] != 0) {
                avgRates[i] = rates[i] / yearCounter[i];
            } else {
                avgRates[i] = 0f;
            }
        }

        XYChart.Series<String, Float> series = new XYChart.Series<>();

        for (int i = 0; i < yearCounter.length; i++) {
            System.out.println(years.get(i) + " - " + rates[i]);
            series.getData().add(new XYChart.Data<>(years.get(i), avgRates[i]));
        }

        lineChart.getData().add(series);
    }
}
