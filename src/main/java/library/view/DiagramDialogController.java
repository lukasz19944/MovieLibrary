package library.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import library.dao.MovieDao;
import library.model.Movie;
import library.util.ArrayUtil;

import java.time.Year;
import java.util.List;

public class DiagramDialogController {

    @FXML
    private LineChart<String, Float> lineChart;
    @FXML
    private CategoryAxis xAxis;

    @FXML
    private CheckBox checkBox;

    private ObservableList<String> years = FXCollections.observableArrayList();
    private ObservableList<String> yearsWithZeroes = FXCollections.observableArrayList();
    private ObservableList<String> yearsWithoutZeroes = FXCollections.observableArrayList();

    private XYChart.Series<String, Float> series = new XYChart.Series<>();
    private XYChart.Series<String, Float> seriesWithZeroes = new XYChart.Series<>();
    private XYChart.Series<String, Float> seriesWithoutZeroes = new XYChart.Series<>();

    private int minYear;
    private int maxYear;


    @FXML
    private void initialize() {
        checkBox.setSelected(false);

        MovieDao mDao = new MovieDao();
        minYear = mDao.getMinYear();
        maxYear = Year.now().getValue();

        checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (checkBox.isSelected()) {
                    series = seriesWithoutZeroes;
                    years = yearsWithoutZeroes;
                } else {
                    series = seriesWithZeroes;
                    years = yearsWithZeroes;
                }
                lineChart.getData().clear();
                lineChart.getData().add(series);
                xAxis.setCategories(years);

            }
        });
    }

    public void setAverageRateForYear(List<Movie> movies) {
        for (int i = minYear; i <= maxYear; i++) {
            yearsWithZeroes.add(String.valueOf(i));
        }

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

        for (int i = 0; i < yearCounter.length; i++) {
            seriesWithZeroes.getData().add(new XYChart.Data<>(yearsWithZeroes.get(i), avgRates[i]));
        }

        lineChart.getData().add(seriesWithZeroes);
    }

    public void setAverageRateForYearWithoutZeroes(List<Movie> movies) {
        MovieDao mDao = new MovieDao();

        for (Integer year : mDao.getAllYears()) {
            yearsWithoutZeroes.add(String.valueOf(year));
        }

        int yearCount = mDao.getYearCount();
        int[] yearCounter = new int[maxYear - minYear];
        float[] rates = new float[maxYear - minYear];
        float[] avgRates = new float[yearCount];

        for (Movie movie : movies) {
            yearCounter[movie.getReleaseDate() - minYear]++;
            rates[movie.getReleaseDate() - minYear] += movie.getRate();
        }

        yearCounter = ArrayUtil.removeZeroes(yearCounter);
        rates = ArrayUtil.removeZeroes(rates);

        for (int i = 0; i < yearCount; i++) {
            avgRates[i] = rates[i] / yearCounter[i];
        }

        for (int i = 0; i < yearCount; i++) {
            seriesWithoutZeroes.getData().add(new XYChart.Data<>(yearsWithoutZeroes.get(i), avgRates[i]));
        }
    }
}
